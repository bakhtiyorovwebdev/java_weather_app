Weather App GUI
Overview
The Weather App GUI is a Java Swing application designed to provide users with real-time weather information. The application allows users to search for weather data based on a location, and it displays various weather metrics such as current conditions, temperature, humidity, and windspeed. The application retrieves weather data from an external API, processes it, and presents it in a user-friendly graphical interface.

Features
Search Functionality: Enter a location in the search field to fetch weather data from the API.
Weather Condition Display: Shows an image representing the current weather condition (e.g., clear, cloudy, rain, snow).
Temperature: Displays the current temperature in Celsius.
Humidity: Shows the humidity percentage.
Windspeed: Displays the windspeed in meters per hour.
Requirements
Java Development Kit (JDK): Version 17 or higher.
json-simple Library: Version 1.1.1 (or compatible) for JSON parsing.
API Key: Obtain an API key from a weather data provider (e.g., OpenWeatherMap, WeatherAPI).
Image Assets: Required images for weather conditions and icons should be placed in the src/assets/ directory.
Installation
Clone the Repository

Ensure you have JDK 17 installed.
Download the json-simple-1.1.1.jar and add it to your project's classpath.
Obtain API Key

Register for an API key from a weather data provider.
Update the WeatherApp class to include the API key and endpoint configuration.
Place Image Assets

Place required image files in the src/assets/ directory. These include images for weather conditions and other icons.
Usage
Compile the Code

Use the following command to compile the code:

bash
Copy code
javac -d out src/WeatherAppGui.java src/WeatherApp.java
Run the Application

Use the following command to run the application:

bash
Copy code
java -cp "out:lib/json-simple-1.1.1.jar" WeatherAppGui
Alternatively, you can run the WeatherAppGui class directly from your IDE.

Code Description
WeatherAppGui.java
Constructor: Initializes the main window and its components.
addGuiComponents(): Configures and adds GUI components such as text fields, labels, and buttons.
parseDoubleSafely(Object obj): Safely converts an object to a double, handling any potential exceptions.
parseLongSafely(Object obj): Safely converts an object to a long, handling any potential exceptions.
loadImage(String resourcePath): Loads an image from the specified path and returns it as an ImageIcon.
WeatherApp.java
API Integration: Contains methods to fetch weather data from an external API. You must implement this class to make HTTP requests to the weather API and process the JSON response.
Data Parsing: Parses the JSON response to extract weather data such as temperature, humidity, windspeed, and weather conditions.
API Integration
API Key: Add your API key to the WeatherApp class where indicated. This key is used to authenticate requests to the weather data provider.
Endpoint Configuration: Update the API endpoint URL in the WeatherApp class to point to your chosen weather data provider's API.
Error Handling
NumberFormatException: Handled in parseDoubleSafely and parseLongSafely methods to deal with invalid number formats.
IOException: Handled in loadImage method to manage cases where image files cannot be read.
API Errors: Handle errors related to API requests and responses, such as invalid API keys, rate limits, or network issues.
Troubleshooting
Images Not Found: Ensure that image files are located in the src/assets/ directory and that paths are correctly specified.
JSON Parsing Errors: Verify that the API response format matches the expected structure.
API Issues: Check API documentation for error codes and ensure that your API key is valid and has not expired.
Contribution
Contributions are welcome! If you find any issues or have suggestions for improvements, please open an issue or submit a pull request.

