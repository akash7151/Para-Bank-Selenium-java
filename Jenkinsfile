pipeline {
    agent any

    parameters {
        choice(name: 'ENV', choices: ['qa', 'stage', 'prod'], description: 'Target environment')
        choice(name: 'BROWSER', choices: ['chrome', 'firefox', 'edge'], description: 'Browser')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run browser headless')
        string(name: 'EMAIL_TO', defaultValue: 'akashdake9@gmail.com,akashdake16@gmail.com', description: 'Report recipients')
    }

    environment {
        CI = 'true'
        PROJECT_SOURCE = 'D:/Selenium_Java_Framwork/Para_Bank_Hybrid_Framwork'
        EMAIL_REPORT_FILE = 'D:/Playwright_Typescript_Framwork/ParaBank_Palywright_WIth_Javascript/.env.report'
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-21.0.11'
        MAVEN_HOME = 'C:\\Program Files\\JetBrains\\IntelliJ IDEA 2026.1.4\\plugins\\maven\\lib\\maven3'
        PATH = "${env.JAVA_HOME}\\bin;${env.MAVEN_HOME}\\bin;${env.PATH}"
        EMAIL_ENABLED = 'true'
        EMAIL_TO = "${params.EMAIL_TO}"
        REPORT_RECIPIENTS = "${params.EMAIL_TO}"
        EMAIL_ATTACH_REPORTS = 'true'
        EMAIL_FAIL_BUILD = 'false'
    }

    options {
        timestamps()
        timeout(time: 120, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20'))
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout (local workspace)') {
            steps {
                deleteDir()
                bat """
                    robocopy "%PROJECT_SOURCE%" . /E /XD .git target .idea .allure test-output /XF *.log
                    if %ERRORLEVEL% LEQ 7 exit /b 0
                    if exist reports rmdir /s /q reports
                    if exist logs rmdir /s /q logs
                    if exist screenshots rmdir /s /q screenshots
                """
            }
        }

        stage('Maven Build') {
            steps {
                bat """
                    "%JAVA_HOME%\\bin\\java" -version
                    "%MAVEN_HOME%\\bin\\mvn.cmd" -version
                    "%MAVEN_HOME%\\bin\\mvn.cmd" clean compile -B
                """
            }
        }

        stage('Execute Tests') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    bat """
                        set EMAIL_ENABLED=false
                        "%MAVEN_HOME%\\bin\\mvn.cmd" test -B -P${params.ENV} -Dbrowser=${params.BROWSER} -Dheadless=${params.HEADLESS} -Denv=${params.ENV} "-Dretry.count=0"
                    """
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                bat '"%MAVEN_HOME%\\bin\\mvn.cmd" allure:report -B'
                archiveArtifacts artifacts: 'target/site/allure-maven-plugin/**,target/allure-results/**,reports/**,screenshots/**,logs/**', allowEmptyArchive: true
            }
        }

        stage('Email Allure Report') {
            steps {
                catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                    bat """
                        set EMAIL_ENABLED=true
                        set EMAIL_TO=${params.EMAIL_TO}
                        set REPORT_RECIPIENTS=${params.EMAIL_TO}
                        set BUILD_URL=${env.BUILD_URL}
                        "%MAVEN_HOME%\\bin\\mvn.cmd" -q exec:java -Dexec.mainClass=com.parabank.automation.utilities.AllureReportMailer -Dexec.classpathScope=compile
                    """
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            script {
                def recipients = params.EMAIL_TO?.trim()
                if (recipients) {
                    try {
                        emailext(
                            subject: "ParaBank Selenium CI - ${env.JOB_NAME} #${env.BUILD_NUMBER} - ${currentBuild.currentResult}",
                            to: recipients,
                            mimeType: 'text/html',
                            body: """
                                <h3>ParaBank Selenium Hybrid Framework</h3>
                                <p><b>Build:</b> ${env.JOB_NAME} #${env.BUILD_NUMBER}</p>
                                <p><b>Result:</b> ${currentBuild.currentResult}</p>
                                <p><b>Environment:</b> ${params.ENV} | <b>Browser:</b> ${params.BROWSER} | <b>Headless:</b> ${params.HEADLESS}</p>
                                <p>Detailed Allure report email is sent by <code>AllureReportMailer</code>.</p>
                                <p><a href="${env.BUILD_URL}">Open Jenkins build</a></p>
                            """,
                            attachmentsPattern: 'reports/email-artifacts/allure-report.zip,logs/framework.log'
                        )
                    } catch (err) {
                        echo "Jenkins emailext skipped: ${err}"
                    }
                }
            }
        }
    }
}
