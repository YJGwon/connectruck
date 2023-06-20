import React from 'react';
import './TruckList.css';

class TruckList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            trucks: [],
            page: 0,
            size: 20,
            hasNext: true,
            isLoading: false,
            isInitialLoad: true // Flag for initial load
        };

        this.scrollRef = React.createRef();
    }

    componentDidMount() {
        this.fetchTruckData();
        this
            .scrollRef
            .current
            .addEventListener('scroll', this.handleScroll);
    }

    componentWillUnmount() {
        this
            .scrollRef
            .current
            .removeEventListener('scroll', this.handleScroll);
    }

    fetchTruckData = () => {
        const {trucks, page, size, isInitialLoad} = this.state;

        this.setState({isLoading: true});

        const url = `${process.env.REACT_APP_API_URL}/api/trucks?page=${page}&size=${size}`;

        fetch(url)
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`api error: ${response.json().title}`);
                }
            })
            .then(data => {
                const {page, trucks} = data;
                this.setState(prevState => ({
                    trucks: isInitialLoad
                        ? trucks
                        : prevState
                            .trucks
                            .concat(trucks),
                    page: page.currentPage + 1,
                    isLoading: false,
                    hasNext: page.hasNext,
                    isInitialLoad: false // Set isInitialLoad to false after the first load
                }));
            })
            .catch(error => {
                console.error('Error fetching truck data:', error);
                if (error.message.startsWith('api error:')) {
                    alert(error.message);
                } else {
                    alert('푸드트럭 목록을 불러오지 못하였습니다');
                }
            });
    }

    handleScroll = () => {
        const {isLoading, hasNext} = this.state;
        const scrollContainer = this.scrollRef.current;

        if (scrollContainer.scrollTop + scrollContainer.clientHeight >= scrollContainer.scrollHeight - 20) {
            if (!isLoading && hasNext) {
                this.fetchTruckData();
            }
        }
    };

    render() {
        const {trucks, isLoading, hasNext} = this.state;

        return (
            <div
                ref={this.scrollRef}
                style={{
                    height: '400px',
                    overflow: 'auto'
                }}>
                <h1>푸드트럭</h1>

                {
                    trucks.map((truck, index) => (
                        <div className="truck-listing" key={index}>
                            <img
                                className="truck-thumbnail"
                                src={truck.thumbnail || 'https://cdn.pixabay.com/photo/2020/06/02/12/12/sample-5250731_1280.png'}
                                alt={truck.name}/>
                            <div className="truck-info">
                                <h2 className="truck-name">{truck.name}</h2>
                                <p className="truck-location">Location: {truck.location}</p>
                                <p className="truck-hours">Operating Hours: {truck.openHour}
                                    - {truck.closeHour}</p>
                            </div>
                        </div>
                    ))
                }
                {isLoading && <p>불러오는 중...</p>}
                {!isLoading && !hasNext && <p>끝</p>}
            </div>
        );
    }
}

export default TruckList;
