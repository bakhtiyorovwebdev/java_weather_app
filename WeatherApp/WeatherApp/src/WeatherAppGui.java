import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGui extends JFrame {
    private JSONObject weatherData;

    public WeatherAppGui() {
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);

        addGuiComponents();
    }

    private void addGuiComponents() {
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15, 15, 351, 45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        add(searchTextField);

        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
        weatherConditionImage.setBounds(0, 125, 450, 217);
        add(weatherConditionImage);

        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(0, 350, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);

        JLabel weatherCondDesc = new JLabel("Cloudy");
        weatherCondDesc.setBounds(0, 405, 450, 36);
        weatherCondDesc.setFont(new Font("Dialog", Font.BOLD, 32));
        weatherCondDesc.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherCondDesc);

        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15, 500, 74, 66);
        add(humidityImage);

        JLabel humidityText = new JLabel("<html><b>Humidity </b>100%</html>");
        humidityText.setBounds(90, 500, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        add(humidityText);

        JLabel windspeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windspeedImage.setBounds(220, 500, 74, 66);
        add(windspeedImage);

        JLabel windspeedText = new JLabel("<html><b> Windspeed</b> 15m/h</html>");
        windspeedText.setBounds(310, 500, 85, 55);
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 15));
        add(windspeedText);

        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 13, 47, 45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = searchTextField.getText().trim();

                if (userInput.isEmpty()) {
                    return;
                }

                weatherData = WeatherApp.getWeatherData(userInput);

                String weatherCondition = (String) weatherData.get("weather_condition");

                switch (weatherCondition) {
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src/assets/clear.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/assets/cloudy.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/assets/snow.png"));
                        break;
                }

                // Safely cast and handle temperature
                Object tempObj = weatherData.get("temperature");
                double temperature = parseDoubleSafely(tempObj);
                temperatureText.setText(temperature + " C");

                weatherCondDesc.setText(weatherCondition);

                // Safely cast and handle humidity
                Object humidityObj = weatherData.get("humidity");
                long humidity = parseLongSafely(humidityObj);
                humidityText.setText("<html><b>Humidity </b>" + humidity + "%</html>");

                // Safely cast and handle windspeed
                Object windspeedObj = weatherData.get("windspeed");
                double windspeed = parseDoubleSafely(windspeedObj);
                windspeedText.setText("<html><b>Windspeed </b>" + windspeed + "m/h</html>");
            }
        });
        add(searchButton);
    }

    private double parseDoubleSafely(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        } else if (obj instanceof String) {
            try {
                return Double.parseDouble((String) obj);
            } catch (NumberFormatException e) {
                System.err.println("Could not parse double: " + obj);
            }
        }
        return 0; // or another default value
    }

    private long parseLongSafely(Object obj) {
        if (obj instanceof Number) {
            return ((Number) obj).longValue();
        } else if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException e) {
                System.err.println("Could not parse long: " + obj);
            }
        }
        return 0; // or another default value
    }

    private ImageIcon loadImage(String resourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Could not find resource");
        return null;
    }
}
