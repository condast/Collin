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
	event.action = 'DONT_CARE';
	update( event );
	//event.waitUntil(promiseChain);
});

self.addEventListener('notificationclose', function(event) {
	const dismissedNotification = event.notification;
	event.action = 'SHUT_UP';
	update( event );
});

function update( event ){
	console.log('Notification closed!' + event.action);
	const data = event.notification.data;
	console.log(data);
	const url = 'http://localhost:10080/moodle/module/update?id='+ data.userId + '&token=12&notification=' + event.action;
	console.log( url );
	fetch(url , {
		method: 'get',
		headers: {
			'Content-type': 'application/json'
		}
	}).then( function(response){
		if (!response.ok) {
			console.log(response.status );
			throw new Error('An error occurred' + response.status);
		}
		console.log('Response received');
		return response;
	});	
}