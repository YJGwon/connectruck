import React, {useState, useEffect, useContext} from 'react';
import {Typography, List, ListItem, ListItemText, Button} from '@mui/material';

import {CartContext} from '../../../context/CartContext';

export default function ServiceMenuList({truckId}) {
    const [menus, setMenus] = useState([]);
    const {addToCart} = useContext(CartContext);

    useEffect(() => {
        fetchMenus(truckId);
    }, []);

    const fetchMenus = (truckId) => {
        const url = `${process.env.REACT_APP_API_URL}/api/trucks/${truckId}/menus`;

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
                메뉴
            </Typography>
            <List>
                {
                    menus.map((menu, index) => (
                        <ListItem key={index}>
                            <ListItemText primary={menu.name} secondary={`${menu.price}원`}/>
                            <Button 
                                variant="contained" 
                                color="primary"
                                onClick={() => addToCart(menu, truckId)}>
                                담기
                            </Button>
                        </ListItem>
                    ))
                }
            </List>
        </section>
    );
};
