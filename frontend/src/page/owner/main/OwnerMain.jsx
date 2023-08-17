import React from 'react';
import {Container, Button} from '@mui/material';
import {initializeApp} from "firebase/app";
import {getMessaging, getToken, onMessage} from "firebase/messaging";

import './OwnerMain.css';

export const OwnerMain = () => {
    const firebaseConfig = {
        apiKey: 'AIzaSyCnPSnVjn24A4MqVqDYCTX6tKHMUdcyErM',
        authDomain: 'project-connectruck.firebaseapp.com',
        projectId: 'project-connectruck',
        storageBucket: 'project-connectruck.appspot.com',
        messagingSenderId: '648376554902',
        appId: '1:648376554902:web:2b661800d226982d7d79b7'
    };
    initializeApp(firebaseConfig);

    const messaging = getMessaging();
    onMessage(messaging, (payload) => {
        console.log('Message received. ', payload);
    });

    const enableFcm = () => {
    if (!("Notification" in window)) {
        alert("주문 알림이 지원되지 않는 브라우저입니다.");
        return;
    }
    Notification
        .requestPermission()
        .then((permission) => {
            if (permission === "granted") {
                getToken(messaging, {vapidKey: `${process.env.REACT_APP_FIREBASE_VAPID_KEY}`}).then((currentToken) => {
                    if (currentToken) {
                        alert("주문 알림이 활성화됩니다.");
                        console.log("token", currentToken);
                    } else {
                        alert("토큰을 가져올 수 없습니다.");
                    }
                }).catch((err) => {
                    console.log('An error occurred while retrieving token. ', err);
                });
            } else {
                alert("모든 알림이 차단되었습니다.");
            }
        });
    }

    return (
        <Container>
            <h4>주문 push 알림 활성화를 위해 사이트의 브라우저 알림 권한을 허용해주세요.</h4>
            <Button variant="contained" onClick={() => enableFcm()}>알림 권한 허용</Button>
        </Container>
    );
}
