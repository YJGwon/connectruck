import React, {useState, useEffect} from 'react';
import { Routes, Route, useParams } from 'react-router-dom';

import ServiceMenuList from '../menulist/ServiceMenuList';

export default function ServiceTruck() {
    const [name, setName] = useState("");

    const { truckId } = useParams();

    
    useEffect(() => {
        fetchTruck(truckId);
    }, []);

    const fetchTruck = (truckId) => {
        const url = `${process.env.REACT_APP_API_URL}/api/trucks/${truckId}`;

        fetch(url)
            .then(async response => {
                const data = await response.json();
                if (response.ok) {
                    return data;
                } else {
                    throw new Error(`api error(${data.title}): ${data.detail}`);
                }
            })
            .then(data => {
                setName(data.name);
            })
            .catch(error => {
                console.error('Error fetching truck data:', error);
                if (error.message.startsWith('api error')) {
                    alert(error.message);
                } else {
                    alert('푸드트럭 정보를 불러오지 못하였습니다');
                }
            });
    }


    return (
        <div>
            <h1>{`${name} 상세 페이지`}</h1>
            <Routes>
                <Route exact='exact' path='/' element={<ServiceMenuList truckId={truckId} />} />
            </Routes>
        </div>
    );
}
