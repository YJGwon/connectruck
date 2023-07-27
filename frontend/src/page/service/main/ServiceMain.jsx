import React, {useState, useEffect} from 'react';
import {Routes, Route, useParams} from 'react-router-dom';

import {fetchData} from '../../../function/CustomFetch';
import TopBar from '../../../component/topbar/TopBar';
import SimpleSideBarButton from '../../../component/topbar/button/SimpleSideBarButton';
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
        const onSuccess = (data) => {
            setName(data.name);
        };

        fetchData({url}, onSuccess);
    }

    const title = `Connectruck 🚚 - ${name}`;
    const root = `/events/${eventId}`;
    const sideButtons  = (
        <React.Fragment>
            <SimpleSideBarButton index={0} link={`${root}/cart`} name='장바구니'/>
        </React.Fragment>
    );

    return (
        <div>
            <TopBar title={title} root={root} buttons={sideButtons}/>
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
