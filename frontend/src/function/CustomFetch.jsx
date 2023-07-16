export const fetchData = async ({url, requestInfo}, onSuccess) => {
    await fetch(url, requestInfo)
        .then(async response => {
            const data = await response.json();

            if (response.ok) {
                return data;
            } else {
                throw new Error(`api error(${data.title}): ${data.detail}`);
            }
        })
        .then(data => {
            onSuccess(data);
        })
        .catch(error => {
            if (error.message.startsWith('api error')) {
                alert(error.message);
            } else {
                console.error('Error fetching data:', error);
                alert('데이터를 불러오지 못하였습니다');
            }
        });
};

export const fetchApi = async ({ url, requestInfo }, onSuccess) => {
    await fetch(url, requestInfo)
        .then(async response => {
            if (response.ok) {
                onSuccess();
            } else {
                const data = await response.json();
                throw new Error(`api error(${data.title}): ${data.detail}`);
            }
        })
        .catch(error => {
            if (error.message.startsWith('api error')) {
                alert(error.message);
            } else {
                console.error('Error fetching api:', error);
                alert('처리하지 못하였습니다');
            }
        });
};
