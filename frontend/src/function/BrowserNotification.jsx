export const requestNotificationPermission = () => {
    if (!("Notification" in window)) {
        alert("주문 알림이 지원되지 않는 브라우저입니다.");
        return;
    }
    if (Notification.permission === "granted") {
        return;
    }

    Notification
        .requestPermission()
        .then((permission) => {
            if (permission === "granted") {
                alert("주문 알림이 활성화됩니다.");
            } else {
                alert("모든 알림이 차단되었습니다.");
            }
        });
} 

export const sendNotification = (message) => {
    if (!("Notification" in window)) {
        return;
    }

    if (Notification.permission === "granted") {
        new Notification(message);
    }
};
