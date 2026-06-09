# CoopBank Loan App

An Android application for managing and applying for loans, built with Jetpack Compose.

## Features

- **Home Dashboard**: View active loan balances, monthly payments, and interest.
- **Loan Catalog**: Browse and apply for various loan types (e.g., BNPL, Salary E-Loan).
- **Loan Application**: Seamless flow for applying and confirming loans.

## Tech Stack

- **UI**: Jetpack Compose
- **Architecture**: MVVM
- **Database**: Room (for local persistence)
- **Navigation**: Compose Navigation
- **Lifecycle**: ViewModel, StateFlow

## Getting Started

### Prerequisites

- Android Studio Jellyfish | 2023.3.1 or newer
- JDK 17
- Android SDK 34 (API Level 34)

### Building the Project

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd coopbank
   ```

2. Open the project in Android Studio.

3. Sync Gradle projects:
   Android Studio should automatically prompt for a Gradle sync. If not, go to `File > Sync Project with Gradle Files`.

4. Build the project:
   ```bash
   ./gradlew assembleDebug
   ```

### Running the App

1. Connect an Android device or start an emulator (API 24+ recommended).
2. Click the **Run** button in Android Studio or use the command line:
   ```bash
   ./gradlew installDebug
   ```

## Project Structure

- `app/src/main/java/com/coopbank/loanapp/ui`: UI components and screens using Jetpack Compose.
- `app/src/main/java/com/coopbank/loanapp/domain`: Domain models and business logic.
- `app/src/main/java/com/coopbank/loanapp/data`: Data sources and Room database configuration.
- `app/src/main/java/com/coopbank/loanapp/viewmodel`: ViewModels for state management.

## Troubleshooting

If you encounter resources build errors (e.g., `SAXParseException`), ensure all XML files in `res/values/` are properly terminated. (Note: A common issue in `themes.xml` where `</style>` was used instead of `</resources>` has been addressed).
