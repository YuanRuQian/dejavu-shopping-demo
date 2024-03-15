## Optimistic UI + Throttling: Like Button

- Optimistic UI: The like button is updated immediately after the user clicks on it, without waiting for the server response.
- Throttling: The change like status request is throttled (250 ms) to prevent frequent on-off clicks to overload the server.

![Optimistic UI + Throttling: Like Button](./screenshots/throttled_liked_button.gif)

## Search Bar with Debounced API Request

![Search Bar with Debounced API Request](./screenshots/debouce_search_bar.gif)

## Address Auto Complete

![Address Auto Complete](./screenshots/address_auto_complete.gif)

## API request with Pagination 

![API request with Pagination](./screenshots/swipe-to-refresh.gif)

## How to Test Token Storage Features

- Git clone the repository: https://github.com/bezkoder/node-js-jwt-auth-mongodb
- Start the node server
- Use Postman to register, login, and logout to get the token
- Setup token in your `secrets.properties`
- Run the app and test the token storage features
