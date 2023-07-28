import React from 'react';
import {Badge} from '@mui/material';
import {Menu} from '@mui/icons-material';

export default function BadgedMenuIcon({badgeContent}) {
    return (
        <Badge badgeContent={badgeContent} color='warning'>
            <Menu/>
        </Badge>
    );
};
