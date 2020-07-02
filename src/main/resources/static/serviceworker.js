var cacheName = 'v1';

var staticAssets = [
    "/css/main.css",
    "/img/icons/icon-192x192.png",
    "/img/icons/icon-512x512.png",
    "/js/app.js",
    "/js/capture.js",
    "/favicon.ico",
    "/index.html",
    "/addtouristspot.html",
    "/touristspot.html",
    "/avisos.html",
    "/manifest.json",
    "/serviceworker.js",
    "/webjars/bootstrap/css/bootstrap.min.css",
    "/webjars/bootstrap/js/bootstrap.min.js",
    "/webjars/sockjs-client/sockjs.min.js",
    "/webjars/stomp-websocket/stomp.min.js",
    "/webjars/jquery/jquery.min.js"
    ];


//Cache async
self.addEventListener('install', async event => { //Cache async
    const cache = await caches.open(cacheName);
    await cache.addAll(staticAssets);
});

//https://developers.google.com/web/ilt/pwa/caching-files-with-service-worker

/*  //Cache sync
self.addEventListener('install', function(event) { //Cache sync
  event.waitUntil(
    caches.open(cacheName).then(function(cache) {
      return cache.addAll(staticAssets);
    })
  );
});*/

//Caso trocar o Service Worker verificar o cache e eliminar os não necessários
self.addEventListener('activate', async event => {
    console.log('activated');
    event.waitUntil(
        caches.keys().then(function(cacheNames) {
          return Promise.all(
            cacheNames.filter(function(cacheName) {
              // Return true if you want to remove this cache,
              // but remember that caches are shared across
              // the whole origin
            }).map(function(cacheName) {
              return caches.delete(cacheName);
            })
          );
        })
      );
});
//Cache first & not found go network
//Cache then network (Classic)
self.addEventListener('fetch', function(event) {
console.log('Fetching Novo Modo:', event.request.url);
  event.respondWith(
    caches.match(event.request)
      .then(function(response) {
        // Cache hit - return response
        if (response) {
          return response;
        }
        return fetch(event.request);
      }
    )
  );
});
//Cache then network and renew cache with more data
/*
self.addEventListener('fetch', function(event) {
  event.respondWith(
    caches.open(cacheName).then(function(cache) {
      return cache.match(event.request).then(function (response) {
        return response || fetch(event.request).then(function(response) {
          cache.put(event.request, response.clone());
          return response;
        });
      });
    })
  );
});*/