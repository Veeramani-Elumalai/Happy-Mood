# Happy Mood - Android Joke App 😊

A fun Android application that generates personalized jokes with emojis based on your current mood!

## Features

- **🌐 Online Joke Fetching**: Fetches fresh jokes from multiple online APIs (JokeAPI, Chuck Norris API)
- **📱 Offline Support**: Falls back to local jokes when internet is unavailable
- **Mood-Based Jokes**: Choose from 4 different moods (Happy, Sad, Excited, Calm) to get personalized jokes
- **Emoji Integration**: Every joke comes with a relevant emoji to enhance the experience
- **Non-Repetitive**: The app ensures you don't see the same joke twice in a row
- **Custom Jokes**: Add your own jokes with custom emojis and categorize them by mood
- **Persistent Storage**: Your custom jokes and online jokes are saved and persist between app sessions
- **Loading States**: Visual feedback while fetching online jokes
- **Smart Fallback**: Automatically switches between online and offline modes
- **GIF Integration**: Displays mood-specific GIFs from Giphy alongside jokes

## Setup

### Giphy API Key Configuration

The app uses Giphy API to fetch mood-specific GIFs. To set up your own API key:

1. **Get a Giphy API Key** (free):
   - Visit [Giphy Developers Portal](https://developers.giphy.com/)
   - Sign up for a free account
   - Create a new app to get your API key

2. **Configure the API Key**:
   - Copy `app/api_keys.properties.example` to `app/api_keys.properties`
   - Open `app/api_keys.properties` and replace `YOUR_GIPHY_API_KEY_HERE` with your actual API key
   - The file should look like: `GIPHY_API_KEY=your_actual_key_here`

3. **Note**: 
   - The app will work with a demo API key if you don't configure your own
   - However, using your own key is recommended for better rate limits and full functionality
   - The `api_keys.properties` file is gitignored and won't be committed to version control

## How to Use

1. **Choose Your Mood**: Tap on one of the four mood buttons (Happy 😄, Sad 😢, Excited 🤩, Calm 😌)
2. **Enter Mood Page**: Each mood opens its own dedicated page with themed colors and styling
3. **Get Your Joke**: The app will display a random joke with an emoji that matches your selected mood
4. **Navigate Jokes**: Use the "Next Joke" button to get more jokes in the same mood category
5. **Add Custom Jokes**: Tap "Add [Mood] Joke ➕" to create and save jokes for that specific mood
6. **Back Navigation**: Use the "Back" button to return to the main menu
7. **Enjoy**: Keep tapping "Next Joke" to explore more jokes in your chosen mood!

## Technical Details

- **Language**: Java
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Storage**: Uses SharedPreferences with Gson for JSON serialization
- **Architecture**: Simple MVC pattern with Activities and Manager classes
- **Network**: HTTP requests using HttpURLConnection with AsyncTask
- **APIs**: JokeAPI (primary), Chuck Norris API (fallback), Giphy API (for GIFs)
- **Image Loading**: Glide library for GIF loading
- **Permissions**: INTERNET, ACCESS_NETWORK_STATE

## Project Structure

```
app/src/main/java/com/example/happymood/
├── MainActivity.java          # Main activity with mood selection
├── BaseMoodActivity.java      # Base class for mood activities
├── HappyActivity.java         # Happy mood page
├── SadActivity.java           # Sad mood page
├── ExcitedActivity.java       # Excited mood page
├── CalmActivity.java          # Calm mood page
├── AddJokeActivity.java       # Activity for adding custom jokes
├── Joke.java                  # Model class for jokes
├── JokeManager.java           # Manages joke storage and retrieval
├── JokeApiService.java        # Handles online joke fetching
└── NetworkUtils.java          # Network utility functions

app/src/main/res/
├── layout/
│   ├── activity_main.xml     # Main activity layout
│   ├── activity_happy.xml    # Happy mood page layout
│   ├── activity_sad.xml      # Sad mood page layout
│   ├── activity_excited.xml  # Excited mood page layout
│   ├── activity_calm.xml     # Calm mood page layout
│   └── activity_add_joke.xml # Add joke activity layout
├── values/
│   ├── strings.xml           # App strings
│   └── colors.xml           # App colors
└── drawable/                 # UI drawable resources
```

## Default Jokes

The app comes pre-loaded with 20 default jokes (5 for each mood category):
- **Happy**: Funny puns and light-hearted jokes
- **Sad**: Uplifting and encouraging messages
- **Excited**: Energetic and playful jokes
- **Calm**: Relaxed and peaceful humor

## Building and Running

1. Open the project in Android Studio
2. Sync the project with Gradle files
3. Run the app on an emulator or physical device
4. Enjoy your personalized jokes!

## Future Enhancements

- Voice narration for jokes
- Joke sharing functionality
- More mood categories
- Joke rating system
- Daily joke notifications

---

Made with ❤️ for spreading happiness and laughter!
