import React, {useState, useEffect} from 'react';
import { Routes, Route, useParams } from 'react-router-dom';

import {fetchData} from '../../../function/CustomFetch';
import ServiceMenuList from '../menulist/ServiceMenuList';

export default function ServiceTruck() {
    const [name, setName] = useState("");

    const { truckId } = useParams();

    
    useEffect(() => {
        fetchTruck(truckId);
    }, []);

    const fetchTruck = (truckId) => {
        const url = `${process.env.REACT_APP_API_URL}/api/trucks/${truckId}`;
        const onSuccess = (data) => {
            setName(data.name);
        };
        fetchData({url}, onSuccess);
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
