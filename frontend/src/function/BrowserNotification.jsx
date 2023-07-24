export const sendNotification = (message) => {
    if (!("Notification" in window)) {
        return;
    }

    if (Notification.permission !== "granted") {
        Notification
            .requestPermission()
            .then((permission) => {
                if (permission === "granted") {
                    new Notification(message);
                } else {
                    alert("모든 알림이 차단되었습니다.");
                    return;
                }
            });
    } else {
        new Notification(message);
    }
};
