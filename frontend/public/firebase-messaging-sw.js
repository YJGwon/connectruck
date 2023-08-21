importScripts('https://www.gstatic.com/firebasejs/8.10.1/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.10.1/firebase-messaging.js');

const firebaseConfig = {
  apiKey: 'AIzaSyCnPSnVjn24A4MqVqDYCTX6tKHMUdcyErM',
  authDomain: 'project-connectruck.firebaseapp.com',
  projectId: 'project-connectruck',
  storageBucket: 'project-connectruck.appspot.com',
  messagingSenderId: '648376554902',
  appId: '1:648376554902:web:2b661800d226982d7d79b7'
};

firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();
