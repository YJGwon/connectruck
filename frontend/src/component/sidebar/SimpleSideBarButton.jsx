import React from 'react';
import {ListItemButton, ListItemText} from '@mui/material';

export default function SimpleSideBarButton({index, link, name}) {
    return (
        <ListItemButton 
            key={index}
            href={link}>
            <ListItemText primary={name}/>
        </ListItemButton>
    );
};
