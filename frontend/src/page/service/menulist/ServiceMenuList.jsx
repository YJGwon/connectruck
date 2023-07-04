import React, {useState, useEffect} from 'react';
import {Typography, List, ListItem, ListItemText, Button} from '@mui/material';

export default function ServiceMenuList({truckId}) {
    const [menus, setMenus] = useState([]);

    useEffect(() => {
        fetchMenus(truckId);
    }, []);

    const fetchMenus = (truckId) => {
        const url = `${process.env.REACT_APP_API_URL}/api/trucks/${truckId}/menus`;

        fetch(url)
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error(`api error(${response.json().title}): ${response.json().detail}`);
                }
            })
            .then(data => {
                setMenus(data.menus);
            })
            .catch(error => {
                console.error('Error fetching menu data:', error);
                if (error.message.startsWith('api error')) {
                    alert(error.message);
                } else {
                    alert('메뉴 목록을 불러오지 못하였습니다');
                }
            });
    }

    return (
        <section id="menu-items">
            <Typography variant="h5" component="h2">
                {truckId}번 Menu
            </Typography>
            <List>
                {
                    menus.map((menu, index) => (
                        <ListItem key={index}>
                            <ListItemText primary={menu.name} secondary={`${menu.price}원`}/>
                            <Button variant="contained" color="primary">담기</Button>
                        </ListItem>
                    ))
                }
            </List>
        </section>
    );
};
