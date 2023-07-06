import React, {useState, useEffect} from 'react';
import {Routes, Route, useParams} from 'react-router-dom';

import TopBar from '../../../component/topbar/TopBar';
import ServiceTruckList from '../trucklist/ServiceTruckList';
import ServiceTruck from '../truck/ServiceTruck';
import ServiceCart from '../cart/ServiceCart';

export default function ServiceMain() {
    const [name, setName] = useState("");

    const {eventId} = useParams();

    useEffect(() => {
        fetchEvent(eventId);
    }, []);

    const fetchEvent = (eventId) => {
        const url = `${process.env.REACT_APP_API_URL}/api/events/${eventId}`;

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
                console.error('Error fetching event data:', error);
                if (error.message.startsWith('api error')) {
                    alert(error.message);
                } else {
                    alert('행사 정보를 불러오지 못하였습니다');
                }
            });
    }

    const title = `${name} 푸드트럭 주문 by Connectruck 🚚`;
    const root = `/events/${eventId}`;
    const buttons = [
        {
            link: `${root}/cart`,
            name: '장바구니'
        }
    ];

    return (
        <div>
            <TopBar title={title} root={root} buttons={buttons}/>
            <div className="container">
                <Routes>
                    <Route
                        exact='exact'
                        path='/'
                        element={<ServiceTruckList eventId = {
                            eventId
                        } />
                        }
                    />
                    <Route path='/trucks/:truckId/*' element={<ServiceTruck />}/>
                    <Route path='/cart' element={<ServiceCart />}/>
                </Routes>
            </div>
        </div>
    );
}
