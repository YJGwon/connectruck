import React from 'react';
import {ListItem, ListItemButton, ListItemText} from '@mui/material';

export default function SimpleSideBarButton({index, link, name}) {
    return (
        <ListItem key={index} disablePadding={true}>
            <ListItemButton href={link}>
                <ListItemText primary={name}/>
            </ListItemButton>
        </ListItem>
    );
};
