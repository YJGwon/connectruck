import React, {useContext} from 'react';
import {Container, Button} from '@mui/material';
import {initializeApp} from "firebase/app";
import {getMessaging, getToken, onMessage} from "firebase/messaging";

import {UserContext} from '../../../context/UserContext';
import {fetchApi} from '../../../function/CustomFetch';

import './OwnerMain.css';

export const OwnerMain = ({onOrder}) => {
    const {accessToken} = useContext(UserContext);

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
        alert(payload.notification.title);
        const order = {'id': Number(payload.data.orderId), 'status': payload.data.status};
        onOrder(order);
    });

    const enableFcm = () => {
        getFcmToken(subscribeOrders);
    }

    const disableFcm = () => {
        getFcmToken(unsubscribeOrders);
    }

    const subscribeOrders = async (token) => {
        const url = `${process.env.REACT_APP_API_URL}/api/notification/orders/my/subscription`;
        const data = {token};
        const requestInfo = {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        };
        const onSuccess = () => {
            alert("주문 알림이 활성화됩니다.");
        };
        fetchApi({url, requestInfo}, onSuccess);
    };

    const unsubscribeOrders = async (token) => {
        const url = `${process.env.REACT_APP_API_URL}/api/notification/orders/my/subscription`;
        const data = {token};
        const requestInfo = {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        };
        const onSuccess = () => {
            alert("주문 알림이 해지되었습니다.");
        };
        fetchApi({url, requestInfo}, onSuccess);
    };

    const getFcmToken = (onToken) => {
        if (!("Notification" in window)) {
            alert("알림이 지원되지 않는 브라우저입니다.");
            return;
        }

        Notification
            .requestPermission()
            .then((permission) => {
                if (permission === "granted") {
                    getToken(messaging, {vapidKey: `${process.env.REACT_APP_FIREBASE_VAPID_KEY}`}).then((currentToken) => {
                        if (currentToken) {
                            onToken(currentToken);
                        } else {
                            throw new Error("브라우저 토큰을 가져올 수 없습니다.");
                        }
                    }).catch((err) => {
                        alert("브라우저 토큰을 가져올 수 없습니다.");
                        console.log('An error occurred while retrieving token. ', err);
                    });
                } else {
                    alert("브라우저 알림이 차단되었습니다.");
                }
            });
    }

    return (
        <Container>
            <h4>주문 push 알림 활성화를 위해 사이트의 브라우저 알림 권한을 허용해주세요.</h4>
            <Button variant="contained" onClick={() => enableFcm()}>알림 권한 허용</Button>
            <h4>브라우저 창을 닫아도 push 알림을 받을 수 있어요. 더이상 받고 싶지 않다면 push 알림을 해지하세요.</h4>
            <Button variant="contained" onClick={() => disableFcm()}>push알림 해지</Button>
        </Container>
    );
}
