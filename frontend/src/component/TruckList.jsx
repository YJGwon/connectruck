import React from 'react';

class TruckList extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      trucks: [],
      page: 0, // Current page number
      size: 20, // Number of restaurants to display per page
      hasNext: true
    };
  }

  componentDidMount() {
    // Fetch truck data from API and update the state
    this.fetchTruckData();
  }

  fetchTruckData() {
    const { page, size } = this.state;

    // Construct the API endpoint URL with request parameters
    const url = `http://localhost:8080/api/trucks?page=${page}&size=${size}`;

    // Fetch restaurant data from API
    fetch(url)
      .then(response => response.json())
      .then(data => {
        const { trucks } = data;
        this.setState({
          trucks: trucks
        });
      })
      .catch(error => {
        console.error('Error fetching restaurant data:', error);
      });
  }

  render() {
    const { trucks: trucks } = this.state;

    return (
      <div>
        <h1>푸드트럭</h1>

        {trucks.map((truck, index) => (
          <div className="truck-listing" key={index}>
            <img className="truck-thumbnail" src={truck.thumbnail} alt={truck.name} />
            <div className="truck-info">
                <h2 className="truck-name">{truck.name}</h2>
                <p className="truck-location">Location: {truck.location}</p>
                <p className="truck-hours">Operating Hours: {truck.openHour} - {truck.closeHour}</p>
            </div>
          </div>
        ))}
      </div>
    );
  }
}

export default TruckList;
