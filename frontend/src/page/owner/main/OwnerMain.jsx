import React from 'react';
import {Container, Button} from '@mui/material';
import {initializeApp} from "firebase/app";
import {getMessaging, getToken} from "firebase/messaging";

import './OwnerMain.css';

export const OwnerMain = () => {
    const firebaseConfig = {
        apiKey: `${process.env.REACT_APP_FIREBASE_API_KEY}`,
        authDomain: `${process.env.REACT_APP_FIREBASE_AUTH_DOMAIN}`,
        projectId: `${process.env.REACT_APP_FIREBASE_PROJECT_ID}`,
        storageBucket: `${process.env.REACT_APP_FIREBASE_STORAGE_BUCKET}`,
        messagingSenderId: `${process.env.REACT_APP_FIREBASE_MESSAGING_SENDER_ID}`,
        appId: `${process.env.REACT_APP_FIREBASE_APP_ID}`
    };
    
    const app = initializeApp(firebaseConfig);
    const messaging = getMessaging(app);

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
