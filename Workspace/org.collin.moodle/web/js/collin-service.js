self.addEventListener('push', function(event) {

	const payload = event.data ? event.data.text() : 'no payload';
	console.log( payload);

	const options = JSON.parse(payload);

//	Keep the service worker alive until the notification is created.
	event.waitUntil(
			self.registration.showNotification( options.title, options)
	);
});