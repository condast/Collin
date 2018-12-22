/**
 * Show the push notification
 * @param event
 * @returns
 */
self.addEventListener('push', function(event) {
	const payload = event.data ? event.data.text() : 'no payload';
	if( payload == null )
		return;

	const options = JSON.parse(payload);

	const maxVisibleActions = Notification.maxActions; 
	console.log( options.title);
	if ( options.actions.length > maxVisibleActions) { 
		options.body = 'This notification will only display' +
		'${ maxVisibleActions} actions.';
	}

	//	Keep the service worker alive until the notification is created.
	event.waitUntil(
			self.registration.showNotification( options.title, options)
	);
});


/**
 * Respond to clicking on the notification
 * @param event
 * @returns
 */
self.addEventListener('notificationclick', function(event) {
	const clickedNotification = event.notification;
	clickedNotification.close();

	// Do something as the result of the notification click
	doSomething( event );
	//event.waitUntil(promiseChain);
});

self.addEventListener('notificationclose', function(event) {
	const dismissedNotification = event.notification;
	doSomething( event );
});

function doSomething( event ){
	console.log('Notification closed!' + event.action);
	const data = event.notification.data;
	console.log('');
	console.log('The data notification had the following parameters:');
	console.log( data );
	fetch('http://localhost:10080/moodle/module/update?id='+ data.userid + '&token=12&notification=' + event.action , {
		method: 'get',
		headers: {
			'Content-type': 'application/json'
		}
	}).then( function(response){
		if (!response.ok) {
			throw new Error('An error occurred')
		}
		console.log('Response received');
		return response;
	});	
}