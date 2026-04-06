# Mondial CRX Web Automation Framework

A Selenium + TestNG + Page Object Model (POM) based automated testing framework for the Mondial CRX application.

---

## Tech Stack

| Tool | Version |
|------|---------|
| Java | 11 |
| Selenium | 4.16.1 |
| TestNG | 7.8.0 |
| WebDriverManager | 5.6.2 |
| ExtentReports | 5.1.1 |
| Maven | 3.x |

---

## Project Structure

```
web-automation-framework/
├── src/
│   ├── main/
│   │   ├── java/com/mondial/
│   │   │   ├── pages/          # Page Object classes
│   │   │   └── utils/          # Utilities (DriverManager, ConfigReader, ExtentReportManager)
│   │   └── resources/
│   │       └── config.properties  # Test configuration (URL, credentials, company)
│   └── test/
│       ├── java/com/mondial/
│       │   ├── tests/
│       │   │   ├── BaseTest.java         # Base test class (driver setup/teardown)
│       │   │   ├── login/                # Login & user creation tests
│       │   │   ├── company/              # Company module tests (COA, Segments, Customers, Vendors, Accounting Period)
│       │   │   ├── enterprise/           # Enterprise Setup tests (Ledger, Alternate Accounts, Users)
│       │   │   ├── reports/              # Report Writer module tests (RowFormat, ColumnLayout, FormatCodes, Headers, Footers)
│       │   │   ├── integrations/         # End-to-end integration tests
│       │   │   └── templated/report/     # Templated report verification tests
│       │   └── listeners/
│       │       └── TestListener.java     # TestNG listener for ExtentReports
│       └── resources/
│           ├── Regression.xml            # Full regression test suite
│           └── testng-FixedTests.xml     # CI stable test suite
├── pom.xml
└── README.md
```

---

## Test Modules

### Login
- `LoginTest` – Valid/invalid login scenarios
- `UserCreationTest` – New user creation

### Company
- `CR_COACrudTest` / `CR_COAUploadDownloadTest` – Chart of Accounts CRUD and CSV upload/download
- `CR_NaturalAccCRUDTest` – Natural Account Sets
- `CR_SegmentCRUDTest` / `CR_SegmentValidationTest` / `CR_SegmentOptionsAllTest` / `CR_SegmentCSVTemplateTest` – Reporting Segments
- `CR_FieldValidatorsCRUD` – Field Validators CRUD
- `CR_ReportingCurrency_WithoutJournal` / `CR_ReportingCurrency_WithJournal` – Reporting Currency
- `CR_CustomersTest` / `CR_CustomersGridTest` – Customers CRUD and grid operations
- `CR_CustomersSortTest` – Customers grid sort by customer name (ascending/descending)
- `CR_VendorsTest` / `CR_VendorsGridTest` – Vendors CRUD and grid operations
- `CR_AccountingPeriod` – Accounting Period creation and role assignment
- `CR_LockAccountingPeriod` – Lock/Unlock Accounting Period

### Enterprise Setup
- `ES_LedgerCRUDTest` – Ledger CRUD
- `ES_LedgerUnauthorisedAccessTest` – Access control for Ledger
- `ES_AA_CRUDTest` – Alternate Accounts CRUD
- `ES_AA_CopyFromCompanyTest` – Copy Alternate Accounts from OEC Brasil
- `ES_AA_CSVUploadTest` – Alternate Accounts CSV upload
- `ES_Users_CRUD` – Users CRUD (create, edit, delete)
- `ES_Users_ErrorMessages` – User creation error message validation
- `ES_Users_ProvideRole` – Assign role to a user
- `ES_Users_UnauthorisedUser` – Unauthorised user access control

### Reports
- `RP_RowFormat_CRUD` / `RP_RowFormat_InsertRow_CRUD` / `RP_CopyRowFormat` – Row Format tests
- `RP_ColumnLayout_CRUD` / `RP_ColumnLayout_InsertColumn` / `RP_CopyColumnLayout` – Column Layout tests
- `RP_FormatCode_CRUD` / `RP_FormatCode_CheckboxSelection` – Format Codes tests
- `RP_ReportHeader_CRUD` / `RP_ReportHeader_CreateRHRow` – Report Header tests
- `RP_ReportFooter_CRUD` / `RP_ReportFooter_CreateRFRow` – Report Footer tests
- `RP_VerifyReportGeneration_PL` / `RP_VerifyReportGeneration_BS` – P&L and Balance Sheet generation
- `RP_VerifyErrorMessagesforInvalidDetails` – Validation error messages
- `RP_AddingRowFormat_UnauthorisedUser` – Unauthorised access test
- `RP_ErrorMessageForHeaderDeletion` – Header deletion error message

### Integrations
- `IntegrationRowFormatInReport` – Row Format used in Report Writer
- `IntegrationColumnLayoutInReport` – Column Layout used in Report Writer
- `IntegrationReportHeaderInReport` – Report Header used in Report Writer
- `IntegrationReportFooterInReport` – Report Footer used in Report Writer
- `IntegrationLedger_ReportWriterTest` – Ledger integrated with Report Writer
- `IntegrationLedger_TemplatedReportTest` – Ledger integrated with Templated Reports
- `ZendeskTest` – Zendesk integration

### Templated Reports
- `I_VerifyTransactionHistory` – Transaction history
- `II_VerifyBalanceDetails_TrialBalance` – Trial Balance
- `III_VerifyBalanceDetails_CurrentPeriodTB` – Current Period Trial Balance
- `IV_VerifyBalanceDetails_DetailedTrialBalance` – Detailed Trial Balance
- `V_VerifyTrialBalance_AllTxnTypes` – All transaction types
- `VI_VerifyTrialBalance_AccountingPeriod` – Accounting Period filter
- `VII_VerifyTrialBalance_AllCurrencies` – All currencies filter
- `VIII_VerifyTrialBalance_AllLedgers` – All Ledgers filter
- `IX_VerifyTrialBalance_AllNaturalAccountSets` – All Natural Account Sets
- `X_VerifyAllReportTypes` – All report types
- `XI_VerifyDownload` – Report download

---

## Running Tests

### Full Regression Suite
```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/Regression.xml
```

### CI Stable Suite
```bash
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testng-FixedTests.xml
```

### Single Test Class
```bash
mvn test -Dtest=CR_AccountingPeriod
```

---

## Key Configuration (`config.properties`)

| Property | Description |
|---|---|
| `base.url` | Application URL |
| `validUsername` | Admin user email |
| `validPassword` | Admin/Consumer password |
| `Consumer` | Consumer role user email |
| `testCompanyName` | Default test company (`AutomationTest DND`) |
| `companyReport` | Company used for report/sort tests (`OEC US Ltd`) |

---

## Reports

After test execution, ExtentReports HTML report is generated at:
```
target/ExtentReports/ExtentReport.html
```

---

## CI/CD

The framework supports GitHub Actions CI/CD. Credentials are injected via GitHub Secrets:
- `VALID_USERNAME`
- `VALID_PASSWORD`

Tests run in headless mode automatically when the `CI` environment variable is detected.
