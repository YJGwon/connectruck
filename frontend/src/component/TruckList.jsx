import React from 'react';

class TruckList extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      trucks: [],
      page: 0,
      size: 20,
      hasNext: true,
      isLoading: false // 추가 트럭 데이터 로딩중인지
    };

    this.fetchTruckData();
    this.scrollRef = React.createRef();
  }

  componentDidMount() {
    // Attach event listener to the scroll container
    this.scrollRef.current.addEventListener('scroll', this.handleScroll);
  }

  componentWillUnmount() {
    // Remove event listener when component is unmounted
    this.scrollRef.current.removeEventListener('scroll', this.handleScroll);
  }

  fetchTruckData = () => {
    const { trucks, page, size } = this.state;

    this.setState({ isLoading: true });

    // Construct the API endpoint URL with request parameters
    const url = `http://localhost:8080/api/trucks?page=${page}&size=${size}`;

    // Fetch restaurant data from API
    fetch(url)
      .then(response => {
        // Check the response status code
        if (response.ok) {
          return response.json();
        } else {
          throw new Error(`api error: ${response.json().title}`);
        }
      })
      .then(data => {
        const { page, trucks } = data;
        this.setState(prevState => ({
          trucks: prevState.trucks.concat(trucks),
          page: page.currentPage + 1,
          isLoading: false,
          hasNext: page.hasNext
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
    const { isLoading, hasNext } = this.state;
    const scrollContainer = this.scrollRef.current;

    // Check if scroll container has reached the bottom
    if (scrollContainer.scrollTop + scrollContainer.clientHeight >= scrollContainer.scrollHeight - 20) {
      // Load more trucks if not currently loading and there are more to load
      if (!isLoading && hasNext) {
        this.fetchTruckData();
      }
    }
  };

  render() {
    const { trucks, isLoading, hasNext } = this.state;

    return (
      <div ref={this.scrollRef} style={{ height: '400px', overflow: 'auto' }}>
        <h1>푸드트럭</h1>

        {trucks.map((truck, index) => (
          <div className="truck-listing" key={index}>
            <img 
              className="truck-thumbnail" 
              src={truck.thumbnail || 'https://cdn.pixabay.com/photo/2020/06/02/12/12/sample-5250731_1280.png'} 
              alt={truck.name} 
            />
            <div className="truck-info">
                <h2 className="truck-name">{truck.name}</h2>
                <p className="truck-location">Location: {truck.location}</p>
                <p className="truck-hours">Operating Hours: {truck.openHour} - {truck.closeHour}</p>
            </div>
          </div>
        ))}
        {isLoading && <p>불러오는 중...</p>}
        {!isLoading && !hasNext && <p>끝</p>}
      </div>
    );
  }
}

export default TruckList;
