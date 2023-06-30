import React from 'react';
import { Typography, List, ListItem, ListItemText, Button } from '@mui/material';

export default function ServiceMenuList({truckId}) {
    return (
        <section id="menu-items">
            <Typography variant="h5" component="h2">
                {truckId}번 Menu
            </Typography>
            <List>
                <ListItem>
                    <ListItemText primary="메뉴" secondary="8000원" />
                    <Button variant="contained" color="primary">담기</Button>
                </ListItem>
                <ListItem>
                    <ListItemText primary="음료수" secondary="3000원" />
                    <Button variant="contained" color="primary">담기</Button>
                </ListItem>
            </List>
        </section>
    );
};
