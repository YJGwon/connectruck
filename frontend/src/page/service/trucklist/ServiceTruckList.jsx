import React, {useState, useEffect, useLayoutEffect, useRef} from 'react';
import {useNavigate} from 'react-router-dom';

import {fetchData} from '../../../function/CustomFetch';
import './ServiceTruckList.css';

export default function ServiceTruckList({eventId}) {
    const [trucks, setTrucks] = useState([]);
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(20);
    const [hasNext, setHasNext] = useState(true);
    const [isLoading, setIsLoading] = useState(false);

    const scrollRef = useRef();
    const navigate = useNavigate();

    useEffect(() => {
        fetchTruckData(eventId);
    }, []);

    useLayoutEffect(() => {
        if (!scrollRef.current) 
            return;
        
        const handleScroll = () => {
            const scrollContainer = scrollRef.current;

            if (scrollContainer.scrollTop + scrollContainer.offsetHeight >= scrollContainer.scrollHeight - 20) {
                if (!isLoading && hasNext) {
                    fetchTruckData(eventId);
                }
            }
        };

        scrollRef
            .current
            .addEventListener('scroll', handleScroll);

        return() => {
            try {
                scrollRef
                    .current
                    .removeEventListener('scroll', handleScroll);
            } catch (e) {
                console.warn('could not removeEventListener on scroll');
            }
        };
    }, [scrollRef.current, isLoading]);

    const fetchTruckData = (eventId) => {
        setIsLoading(true);

        const url = `${process.env.REACT_APP_API_URL}/api/trucks?eventId=${eventId}&page=${page}&size=${size}`;
        const onSuccess = (data) => {
            setTrucks(trucks.concat(data.trucks));
            setPage(data.page.currentPage + 1);
            setIsLoading(false);
            setHasNext(data.page.hasNext);
        };

        fetchData({url}, onSuccess);
    }

    const handleTruckItemClick = (truckId) => {
        navigate(`./trucks/${truckId}`);
    };

    return (
        <div
            ref={scrollRef}
            style={{
                height: '400px',
                overflow: 'auto'
            }}>
            <h1>푸드트럭</h1>

            {
                trucks.map((truck, index) => (
                    <div
                        className="truck-listing"
                        key={index}
                        onClick={() => handleTruckItemClick(truck.id)}
                        style={{ cursor: 'pointer' }}>
                        <img
                            className="truck-thumbnail"
                            src={truck.thumbnail || 'https://cdn.pixabay.com/photo/2020/06/02/12/12/sample-5250731_1280.png'}
                            alt={truck.name}/>
                        <div className="truck-info">
                            <h2 className="truck-name">{truck.name}</h2>
                        </div>
                    </div>
                ))
            }
            {isLoading && <p>불러오는 중...</p>}
            {!isLoading && !hasNext && <p>끝</p>}
        </div>
    );
}
