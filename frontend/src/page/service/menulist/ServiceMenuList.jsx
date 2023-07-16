import React, {useState, useEffect, useContext} from 'react';
import {Typography, List, ListItem, ListItemText, Button} from '@mui/material';

import {CartContext} from '../../../context/CartContext';
import {fetchData} from '../../../function/CustomFetch';

export default function ServiceMenuList({truckId}) {
    const [menus, setMenus] = useState([]);
    const {addToCart} = useContext(CartContext);

    useEffect(() => {
        fetchMenus(truckId);
    }, []);

    const fetchMenus = (truckId) => {
        const url = `${process.env.REACT_APP_API_URL}/api/trucks/${truckId}/menus`;
        const onSuccess = (data) => {
            setMenus(data.menus);
        };
        fetchData({url}, onSuccess);
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
