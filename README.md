# ChallengePlugin

A modern, modular Minecraft Paper plugin for custom challenges, randomizers, and battle modes. Designed for easy extension, configuration, and public use.

## Features
- Multiple challenge types (randomizer, battles, goals, etc.)
- Intuitive GUIs for challenge selection and settings
- Configurable world settings (view distance, simulation distance, PvP, etc.)
- CommandAPI integration for advanced command handling
- Multiverse-Core support (optional)
- English codebase, German in-game messages (all prefixed)

## Installation
1. Download the latest release from the [releases page](https://github.com/your-repo/ChallengePlugin/releases).
2. Place `ChallengePlugin.jar` in your server's `plugins` folder.
3. (Optional) Add Multiverse-Core for multi-world support.
4. Restart your server.

## Usage
- Use `/challenge` to open the main challenge GUI.
- Configure settings via `/challenge settings` or the in-game GUI.
- All configuration is stored in the `config.yml` file generated on first run.

## Commands
- `/challenge` - Opens the main challenge GUI
- `/challenge start` - Starts the selected challenge
- `/challenge stop` - Stops the current challenge
- `/challenge settings` - Opens the settings GUI

## Configuration
Edit the `config.yml` file in the plugin folder to adjust world settings, enable/disable challenges, and more.

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request. For major changes, open an issue first to discuss your ideas.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
