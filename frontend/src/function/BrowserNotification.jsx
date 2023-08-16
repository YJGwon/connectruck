export const sendNotification = (message) => {
    if (!("Notification" in window)) {
        return;
    }

    if (Notification.permission === "granted") {
        new Notification(message);
    }
};
