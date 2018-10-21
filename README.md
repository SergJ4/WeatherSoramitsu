# WeatherSoramitsu

Some project considerations:

1. Weather models (both domain and repository) have day enums. This is not scalable solution. This poor decision was made in order to have time to complete the task

2. There is no way to stop track some city weather (this case was not mentioned in test task)

3. Saving only one weather forecast for one day (openweather gives 5 days forecast devided by 3 hours intervals). 12:00 hours forecast is saved

4. Forecast is saved without time zone consideration

5. Don`t have enough time to handle Moxy unit testing in multimodule project structure. Moxy is awful!!! (Missing com.arellomobile.mvp.MoxyReflector because it is generated in application package, not arellomobile)

6. Openweather API does not return localized City name, that`s why they are all in English

7. Usecases are small and most of them just passes calls to repository without modifying data. But they are usefull while app grows

8. Poor design

9. Project not covered fully with unit tests (unit tests for weather repository available in repository module). And there are no ui tests at all

10. Openweather api does not provide endpoint to fetch all available cities. Only large JSON (minimum 5 Mb), which I don`t want to embed in application. That is why I integrated google places autocomplete widget in order to find cities. And that is why I had to handle "Not found" api error (reuested city is not in openweather database)
