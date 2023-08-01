import React, {useState, useEffect, useContext} from 'react';
import {Typography, List, ListItem, ListItemText, Button, Stack} from '@mui/material';

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
                            <ListItemText primary={menu.name} secondary={menu.description}/>
                            <Stack direction="row" spacing={2}>
                                <Typography>{`${menu.price}원`}</Typography>
                                <Button 
                                    variant="contained" 
                                    color="primary"
                                    disabled={menu.soldOut}
                                    onClick={() => addToCart(menu, truckId)}>
                                    {menu.soldOut? '품절' : '담기'}
                                </Button>
                            </Stack>
                        </ListItem>
                    ))
                }
            </List>
        </section>
    );
};
