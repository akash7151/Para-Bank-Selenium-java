param(
    [string]$JenkinsUrl = "http://localhost:8080",
    [string]$Username = "admin",
    [string]$Password = "admin",
    [string]$JobName = "ParaBank-Selenium-CI",
    [string]$ProjectRoot = "D:\Selenium_Java_Framwork\Para_Bank_Hybrid_Framwork",
    [string]$EmailReportFile = "D:\Playwright_Typescript_Framwork\ParaBank_Palywright_WIth_Javascript\.env.report"
)

function Escape-Xml {
    param([string]$Value)
    return [System.Security.SecurityElement]::Escape($Value)
}

function New-JenkinsSession {
    param([string]$Url, [string]$User, [string]$Pass)
    $session = New-Object Microsoft.PowerShell.Commands.WebRequestSession
    $token = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("${User}:${Pass}"))
    $headers = @{ Authorization = "Basic $token" }

    $crumbResponse = Invoke-RestMethod -Uri "$Url/crumbIssuer/api/json" -Headers $headers -WebSession $session -TimeoutSec 15
    $crumbHeaders = @{
        Authorization = "Basic $token"
        $crumbResponse.crumbRequestField = $crumbResponse.crumb
    }
    return @{
        Session = $session
        Headers = $crumbHeaders
    }
}

$auth = New-JenkinsSession -Url $JenkinsUrl -User $Username -Pass $Password
$headers = $auth.Headers
$session = $auth.Session

Write-Host "Checking Jenkins at $JenkinsUrl ..."
Invoke-RestMethod -Uri "$JenkinsUrl/api/json?tree=mode" -Headers $headers -WebSession $session -TimeoutSec 15 | Out-Null

$jenkinsfile = Get-Content -Path (Join-Path $ProjectRoot "Jenkinsfile") -Raw
$escapedScript = Escape-Xml $jenkinsfile
$jobDescription = "ParaBank Selenium Java Hybrid Framework CI/CD - local workspace robocopy from $ProjectRoot"

$configXml = @"
<?xml version='1.1' encoding='UTF-8'?>
<flow-definition plugin="workflow-job">
  <description>$([System.Security.SecurityElement]::Escape($jobDescription))</description>
  <keepDependencies>false</keepDependencies>
  <properties>
    <jenkins.model.BuildDiscarderProperty>
      <strategy class="hudson.tasks.LogRotator">
        <daysToKeep>-1</daysToKeep>
        <numToKeep>20</numToKeep>
        <artifactDaysToKeep>-1</artifactDaysToKeep>
        <artifactNumToKeep>-1</artifactNumToKeep>
      </strategy>
    </jenkins.model.BuildDiscarderProperty>
    <org.jenkinsci.plugins.workflow.job.properties.DisableConcurrentBuildsJobProperty/>
  </properties>
  <definition class="org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition" plugin="workflow-cps">
    <script>$escapedScript</script>
    <sandbox>true</sandbox>
  </definition>
  <triggers/>
  <disabled>false</disabled>
</flow-definition>
"@

$existing = $false
try {
    Invoke-WebRequest -Uri "$JenkinsUrl/job/$JobName/config.xml" -Headers $headers -WebSession $session -UseBasicParsing -TimeoutSec 15 | Out-Null
    $existing = $true
} catch {
    $existing = $false
}

if ($existing) {
    Write-Host "Updating existing Jenkins job: $JobName"
    Invoke-RestMethod -Method Post -Uri "$JenkinsUrl/job/$JobName/config.xml" -Headers $headers -WebSession $session -Body $configXml -ContentType "application/xml; charset=UTF-8" | Out-Null
} else {
    Write-Host "Creating Jenkins job: $JobName"
    Invoke-RestMethod -Method Post -Uri "$JenkinsUrl/createItem?name=$JobName" -Headers $headers -WebSession $session -Body $configXml -ContentType "application/xml; charset=UTF-8" | Out-Null
}

if (Test-Path $EmailReportFile) {
    Write-Host "Configuring Jenkins SMTP from $EmailReportFile"
    $envMap = @{}
    Get-Content $EmailReportFile | ForEach-Object {
        $line = $_.Trim()
        if ($line -and -not $line.StartsWith("#") -and $line.Contains("=")) {
            $parts = $line.Split("=", 2)
            $envMap[$parts[0].Trim()] = $parts[1].Trim()
        }
    }

    $smtpHost = $envMap["SMTP_HOST"]
    $smtpPort = $envMap["SMTP_PORT"]
    $smtpUser = $envMap["SMTP_USER"]
    $smtpPass = $envMap["SMTP_PASS"]
    $emailTo = $envMap["EMAIL_TO"]

    if ($smtpHost -and $smtpUser -and $smtpPass) {
        $groovy = @"
import jenkins.model.*
import hudson.plugins.emailext.*

def j = Jenkins.instance
def d = j.getDescriptor(ExtendedEmailPublisher.class)
d.setSmtpHost('$smtpHost')
d.setSmtpPort('$smtpPort')
d.setUseSsl(false)
d.setUseTls(true)
d.setCharset('UTF-8')
d.setDefaultSubject('[ParaBank Selenium CI] \$BUILD_STATUS - \$PROJECT_NAME #\$BUILD_NUMBER')
d.setDefaultContent('''<p>Build: \$PROJECT_NAME #\$BUILD_NUMBER</p><p>Status: \$BUILD_STATUS</p><p><a href="\$BUILD_URL">Open build</a></p>''')
d.setDefaultContentType('text/html')
d.setSmtpUsername('$smtpUser')
d.setSmtpPassword('$smtpPass')
d.setDefaultRecipients('$emailTo')
d.save()
j.save()
"@
        try {
            Invoke-RestMethod -Method Post -Uri "$JenkinsUrl/scriptText" -Headers $headers -WebSession $session -Body @{ script = $groovy } -TimeoutSec 30 | Out-Null
            Write-Host "Jenkins SMTP configured."
        } catch {
            Write-Warning "Could not configure Jenkins SMTP via script console: $_"
        }
    }
} else {
    Write-Warning "Email report file not found: $EmailReportFile"
}

Write-Host ""
Write-Host "Jenkins job ready: $JenkinsUrl/job/$JobName"
Write-Host "Build now: $JenkinsUrl/job/$JobName/build?delay=0sec"
