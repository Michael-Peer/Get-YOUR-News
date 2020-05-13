# Get-YOUR-News

## Development in progress - See wireframe below and check the code

![ללא שם](https://user-images.githubusercontent.com/44434337/80923560-39550600-8d8d-11ea-8be4-b2cb1c1423bb.png)

## Cache flow - The app is fully working even without internet conntection at all

The only time that the user needs internt connection to intercat with the app, is at the first time.

When the app launches, we call ```refreshNews``` from the ```Repository``` through the ```viewModel```, in this function we making ```GET``` request to the api, insert the response to local database, and get the data from the database.

From the second time on, we *DO NOT* need internt connection to intercat with the app - When the user launch the app, he gets all the data directly from the local database, and in the meantime, we trying to make network requrst to refrsh the data in the local database


*Work in the background:* There is a worker that run in the background even if the app is clodes, and this worker is responsible to making network requests in order to give the user the freshest data when he launch the app, and in order to "keep" the user phone and money, the worker runs only when the phone is charging *AND* on wifi

Currently the worker is well..working. But I will add the possilbity to the user to highly customize the settings of when and where the worker should run.
