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
│       │   │   ├── enterprise/           # Enterprise Setup tests (Ledger, Alternate Accounts)
│       │   │   ├── reports/              # Report Writer module tests (RowFormat, ColumnLayout, FormatCodes, Headers, Footers)
│       │   │   ├── integrations/         # End-to-end integration tests
│       │   │   └── tempated/report/      # Templated report verification tests
│       │   └── listeners/
│       │       └── TestListener.java     # TestNG listener for ExtentReports
│       └── resources/
│           ├── testng.xml                # Default full suite
│           ├── testng-FixedTests.xml     # CI stable test suite
│           └── testng-accounting-period.xml
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
- `CR_CustomersTest` / `CR_CustomersGridTest` – Customers
- `CR_VendorsTest` / `CR_VendorsGridTest` – Vendors
- `CR_AccountingPeriod` – Accounting Period management
- `CR_LockAccountingPeriod` – Lock/Unlock Accounting Period
- `DiscoverCompanyForm` – Company form discovery

### Enterprise Setup
- `ES_LedgerCRUDTest` – Ledger CRUD
- `ES_LedgerUnauthorisedAccessTest` – Access control for Ledger
- `ES_AA_CRUDTest` – Alternate Accounts CRUD
- `ES_AA_CopyFromCompanyTest` – Copy Alternate Accounts from company
- `ES_AA_CSVUploadTest` – Alternate Accounts CSV upload

### Reports
- `RowFormat_CRUD` / `RowFormat_InsertRow_CRUD` / `CopyRowFormat` – Row Format tests
- `ColumnLayout_CRUD` / `ColumnLayout_InsertColumn` / `CopyColumnLayout` – Column Layout tests
- `FormatCode_CRUD` / `FormatCode_CheckboxSelection` – Format Codes tests
- `ReportHeader_CRUD` / `ReportHeader_CreateRHRow` – Report Header tests
- `ReportFooter_CRUD` / `ReportFooter_CreateRFRow` – Report Footer tests
- `VerifyReportGeneration_PL` / `VerifyReportGeneration_BS` – P&L and Balance Sheet generation
- `VerifyErrorMessagesforInvalidDetails` – Validation error messages
- `AddingRowFormat_UnauthorisedUser` – Unauthorised access test
- `ErrorMessageForHeaderDeletion` – Header deletion error message

### Integrations
- `IntegrationRowFormatInReport` – Row Format used in Report Writer
- `IntegrationColumnLayoutInReport` – Column Layout used in Report Writer
- `IntegrationReportHeaderInReport` – Report Header used in Report Writer
- `IntegrationReportFoorterInReport` – Report Footer used in Report Writer
- `IntegrationLedger_ReportWriterTest` – Ledger integrated with Report Writer
- `IntegrationLedger_TemplatedReportTest` – Ledger integrated with Templated Reports
- `ZendeskTest` – Zendesk integration

### Templated Reports
- `I_VerifyTransactionHistory` – Transaction history
- `II_VerifyBalanceDetails_TrialBalance` – Trial Balance
- `III_VerifyBalanceDetails_CurrentPeriodTB` – Current Period Trial Balance
- `IV_VerifyBalanceDetails_DetailedTrialBalance` – Detailed Trial Balance
- `V_VerifyTrialBalance_AllTxnTypes` – All transaction types
- `VII_VerifyTrialBalance_AccountingPeriod` – Accounting Period filter
- `VIII_VerifyTrialBalance_AllCurrencies` – All currencies filter
- `IX_VerifyTrialBalance_AllNaturalAccountSets` – All Natural Account Sets
- `X_VerifyTrialBalance_AllLedgers` – All Ledgers
- `XI_VerifyAllReportTypes` – All report types
- `XII_VerifyDownload` – Report download

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
