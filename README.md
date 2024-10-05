# Marvel Comics Explorer

Marvel Comics Explorer is a web application that allows users to explore and search through the vast world of Marvel Comics. With a sleek and responsive interface powered by HTML, Tailwind CSS, JavaScript, and a backend built using Spring Boot, users can easily navigate and retrieve data directly from the Marvel API.

## Features

- **Comic Book Search**: Search through thousands of Marvel Comics by title or character.
- **Detailed Comic Information**: View detailed information about each comic, including cover art, publication date, and creators.
- **Responsive Design**: Enjoy a seamless experience across devices, thanks to Tailwind CSS.
- **Powered by Marvel API**: All comic data is fetched in real-time from the official Marvel API.

## Tech Stack

### Frontend

- **HTML**
- **Tailwind CSS**: For styling and responsive design.
- **JavaScript**: Handles API requests and user interactions.

### Backend

- **Spring Boot**: Manages API integration and backend logic.
- **JDK 23**: Developed using the latest Java Development Kit for optimal performance.

### External Services

- **Marvel API**: Fetches data related to comics, characters, and creators.

## Getting Started

### Prerequisites

- **JDK 23**: Ensure Java Development Kit 23 is installed.
- **Maven**: Make sure Maven is installed for building and managing dependencies.
- **Node.js & npm**: Required for running Tailwind CSS and other frontend tooling.

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yusuf7861/Marvel-Comics-Explorer.git
   cd Marvel-Comics-Explorer
   ```

2. Set up your API keys:
   - Create a file named `application.properties` in the `src/main/resources` directory.
   - Add the following:
     ```properties
     marvel.api.public-key=your_public_key
     marvel.api.private-key=your_private_key
     ```

3. Install frontend dependencies:
   ```bash
   npm install
   ```

4. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```

5. Start the frontend:
   ```bash
   npm run dev
   ```

6. Access the application at `http://localhost:3000`.

## API Keys Management

To avoid exposing your API keys in the codebase, consider using a secret management tool like **HashiCorp Vault** for secure storage in production environments.

## Contributing

We welcome contributions to the Marvel Comics Explorer project!

### How to Contribute

1. **Fork** the repository by clicking the "Fork" button on the top right corner of this page.
2. **Clone** the forked repository to your local machine:
   ```bash
   git clone https://github.com/your-username/Marvel-Comics-Explorer.git
   ```
3. **Create a new branch** for your feature or bug fix:
   ```bash
   git checkout -b your-feature-branch
   ```
4. **Make your changes** and commit them with descriptive commit messages.
5. **Push** your changes to your forked repository:
   ```bash
   git push origin your-feature-branch
   ```
6. Submit a **pull request** to the `main` branch of this repository.

### Getting API Keys

For contributors who need access to the Marvel API keys, feel free to email me at **yjamal710@gmail.com** to request the private API key.

## License

This project is licensed under the MIT License. See the LICENSE file for details.
