import React from 'react';
import {ListItemButton, ListItemText, Badge} from '@mui/material';

export default function BadgedSideBarButton({index, link, name, badgeContent}) {
    return (
        <ListItemButton 
            key={index}
            href={link}>
            <ListItemText primary={name}/>
            <Badge badgeContent={badgeContent} color='info'/>
        </ListItemButton>
    );
};
