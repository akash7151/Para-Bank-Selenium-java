"""Generate testdata.xlsx for ParaBank Automation Framework."""
import zipfile
from pathlib import Path
from xml.sax.saxutils import escape

OUTPUT = Path(__file__).resolve().parent / "src" / "main" / "resources" / "testdata.xlsx"

SHEETS = {
    "Login": [
        ["testCase", "username", "password", "expectedError"],
        ["TC02", "invalid_user", "demo", "could not be verified"],
        ["TC03", "john", "wrongpass", "could not be verified"],
        ["TC04", "", "demo", "Please enter a username"],
        ["TC05", "john", "", "Please enter a password"],
    ],
    "Register": [
        ["testCase", "firstName", "lastName", "address", "city", "state", "zipCode", "phone", "ssn", "username", "password", "confirmPassword"],
        ["TC08", "", "", "", "", "", "", "", "", "", "", ""],
        ["TC09", "Jane", "Smith", "789 Pine Rd", "Boston", "MA", "02109", "6175550000", "987-65-4321", "john", "password123", "password123"],
        ["TC10", "Jane", "Smith", "789 Pine Rd", "Boston", "MA", "02109", "6175550000", "987-65-4321", "newuser_test", "password123", "password456"],
    ],
    "TransferFunds": [
        ["testCase", "amount", "fromIndex", "toIndex", "shouldPass"],
        ["Same Account", "100", "0", "0", "false"],
        ["Zero Amount", "0", "0", "1", "false"],
        ["Negative Amount", "-50", "0", "1", "false"],
        ["Large Amount", "999999", "0", "1", "false"],
    ],
    "BillPay": [
        ["testCase", "name", "address", "city", "state", "zipCode", "phone", "accountNumber", "verifyAccount", "amount", "shouldPass"],
        ["Mandatory Fields", "", "", "", "", "", "", "", "", "", "false"],
        ["Invalid Account", "Test Payee", "123 St", "Boston", "MA", "02108", "6175551111", "12345", "54321", "25.00", "false"],
        ["Invalid Zip", "Test Payee", "123 St", "Boston", "MA", "ABC", "6175551111", "12345", "12345", "25.00", "false"],
    ],
    "Loan": [
        ["testCase", "amount", "downPayment", "expectedResult"],
        ["Loan Denied", "100000", "0", "denied"],
        ["Zero Amount", "0", "0", "denied"],
        ["Large Amount", "999999", "100", "denied"],
        ["Invalid Down Payment", "5000", "10000", "denied"],
    ],
}


def col_letter(index: int) -> str:
    result = ""
    while index >= 0:
        result = chr(index % 26 + ord("A")) + result
        index = index // 26 - 1
    return result


def sheet_xml(rows: list[list[str]]) -> str:
    lines = ['<?xml version="1.0" encoding="UTF-8" standalone="yes"?>']
    lines.append('<worksheet xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main">')
    lines.append("<sheetData>")
    for r_idx, row in enumerate(rows, start=1):
        lines.append(f'<row r="{r_idx}">')
        for c_idx, value in enumerate(row):
            cell_ref = f"{col_letter(c_idx)}{r_idx}"
            safe_value = escape(value)
            lines.append(f'<c r="{cell_ref}" t="inlineStr"><is><t>{safe_value}</t></is></c>')
        lines.append("</row>")
    lines.append("</sheetData>")
    lines.append("</worksheet>")
    return "".join(lines)


def build_workbook(path: Path) -> None:
    sheet_names = list(SHEETS.keys())
    workbook_sheets = []
    for idx, name in enumerate(sheet_names, start=1):
        workbook_sheets.append(f'<sheet name="{escape(name)}" sheetId="{idx}" r:id="rId{idx}"/>')

    content_types = [
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>',
        '<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">',
        '<Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>',
        '<Default Extension="xml" ContentType="application/xml"/>',
        '<Override PartName="/xl/workbook.xml" ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml"/>',
    ]
    for idx in range(1, len(sheet_names) + 1):
        content_types.append(
            f'<Override PartName="/xl/worksheets/sheet{idx}.xml" '
            f'ContentType="application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml"/>'
        )
    content_types.append("</Types>")

    workbook_rels = [
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>',
        '<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">',
    ]
    for idx in range(1, len(sheet_names) + 1):
        workbook_rels.append(
            f'<Relationship Id="rId{idx}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet" '
            f'Target="worksheets/sheet{idx}.xml"/>'
        )
    workbook_rels.append("</Relationships>")

    root_rels = (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">'
        '<Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" '
        'Target="xl/workbook.xml"/>'
        "</Relationships>"
    )

    workbook_xml = (
        '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
        '<workbook xmlns="http://schemas.openxmlformats.org/spreadsheetml/2006/main" '
        'xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships">'
        f"<sheets>{''.join(workbook_sheets)}</sheets>"
        "</workbook>"
    )

    path.parent.mkdir(parents=True, exist_ok=True)
    with zipfile.ZipFile(path, "w", compression=zipfile.ZIP_DEFLATED) as archive:
        archive.writestr("[Content_Types].xml", "".join(content_types))
        archive.writestr("_rels/.rels", root_rels)
        archive.writestr("xl/_rels/workbook.xml.rels", "".join(workbook_rels))
        archive.writestr("xl/workbook.xml", workbook_xml)
        for idx, name in enumerate(sheet_names, start=1):
            archive.writestr(f"xl/worksheets/sheet{idx}.xml", sheet_xml(SHEETS[name]))


if __name__ == "__main__":
    build_workbook(OUTPUT)
    print(f"Created {OUTPUT}")
