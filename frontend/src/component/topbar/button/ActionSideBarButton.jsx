import React from 'react';
import {ListItem, ListItemButton, ListItemText} from '@mui/material';

export default function ActionSideBarButton({index, onClick, name}) {
    return (
        <ListItem key={index} disablePadding={true}>
            <ListItemButton onClick={onClick}>
                <ListItemText primary={name}/>
            </ListItemButton>
        </ListItem>
    );
};
