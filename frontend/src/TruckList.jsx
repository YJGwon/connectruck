import React from 'react';

class TruckList extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      trucks: [
        {
          name: "츄로츄로",
          thumbnail: "https://cdn.pixabay.com/photo/2020/06/02/12/12/sample-5250731_1280.png",
          location: "서울 마포구 성산동 509-7",
          openHour: "11:00",
          closeHour: "21:00"
        },
        {
          name: "하와이안 쉬림프",
          thumbnail: "https://cdn.pixabay.com/photo/2020/06/02/12/12/sample-5250731_1280.png",
          location: "서울 마포구 성산동 509-7",
          openHour: "11:00",
          closeHour: "21:00"
        }
      ]
    };
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
