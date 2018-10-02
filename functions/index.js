//import firebase functions modules
const functions = require('firebase-functions');
//import admin module
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);


// Listens for new messages added to messages/:pushId
exports.pushNotification = functions.database.ref('/feedback/{feedId}').onWrite( event => {

    console.log('Push notification event triggered');

    //  Grab the current value of what was written to the Realtime Database.
    var valueTitle = 'Feedback from ' + event.data.child('username').val();
    var valueBody = event.data.child('fstring').val();

    console.log(valueTitle);
    console.log(valueBody);

  // Create a notification
    const payload = {
        notification: {
            title: valueTitle,
            body: valueBody,
            sound: "default"
        },
    };

  //Create an options object that contains the time to live for the notification and the priority
    const options = {
        priority: "high",
        timeToLive: 60 * 60 * 24
    };


    return admin.messaging().sendToTopic("pushNotifications", payload, options);
});