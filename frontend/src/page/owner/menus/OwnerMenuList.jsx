import React, {useState, useEffect, useContext} from 'react';
import {
    Box, 
    Typography, 
    List, 
    ListItem, 
    ListItemText, 
    Stack,
    Switch,
    FormControlLabel,
    IconButton,
    Modal,
    TextField,
    Button
} from '@mui/material';
import {Edit} from '@mui/icons-material';

import {UserContext} from '../../../context/UserContext';
import {fetchData, fetchApi} from '../../../function/CustomFetch';

export const OwnerMenuList = () => {
    const [menus, setMenus] = useState([]);

    const [editingMenuId, setEditingMenuId] = useState(null);

    const [isDescriptionModalOpen, setIsDescriptionModalOpen] = useState(false);
    const [descriptionInput, setDescriptionInput] = useState('');

    const [isSoldOutModalOpen, setIsSoldoutModalOpen] = useState(false);
    const [soldOutInput, setSoldOutInput] = useState(null);

    const {isInitialized, accessToken} = useContext(UserContext);

    useEffect(() => {
        fetchMenus();
    }, [isInitialized]);

    const fetchMenus = () => {
        if (!isInitialized) {
            return;
        }

        const url = `${process.env.REACT_APP_API_URL}/api/owner/menus/my`;
        const requestInfo = {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            }
        };
        const onSuccess = (data) => {
            setMenus(data.menus);
        };
        fetchData({url, requestInfo}, onSuccess);
    }

    const handleDescriptionModalOpen = (menuId) => {
        setEditingMenuId(menuId);
        setIsDescriptionModalOpen(true);
    }

    const handleDescriptionModalClose = () => {
        setEditingMenuId(null);
        setDescriptionInput('');
        setIsDescriptionModalOpen(false);
    }

    const handleSubmitDescription = () => {
        if (descriptionInput.length > 25) {
            alert('25자 이내로 입력하세요.');
            return;
        }

        const url = `${process.env.REACT_APP_API_URL}/api/owner/menus/${editingMenuId}/description`;
        const description = descriptionInput;
        const data = {
            description
        };
        const requestInfo = {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        };
        const onSuccess = () => {
            alert('저장되었습니다.');
            handleDescriptionModalClose();
            fetchMenus();
        };
        fetchApi({url, requestInfo}, onSuccess);
    }

    const handleSoldOutModalOpen = (soldOut, menuId) => {
        setSoldOutInput(soldOut);
        setEditingMenuId(menuId);
        setIsSoldoutModalOpen(true);
    }

    const handleSoldOutModalClose = () => {
        setEditingMenuId(null);
        setSoldOutInput(null);
        setIsSoldoutModalOpen(false);
    }

    const handleSubmitSoldOut = () => {
        const url = `${process.env.REACT_APP_API_URL}/api/owner/menus/${editingMenuId}/sold-out`;
        const soldOut = soldOutInput;
        const data = {
            soldOut
        };
        const requestInfo = {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${accessToken}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        };
        const onSuccess = () => {
            alert('저장되었습니다.');
            handleSoldOutModalClose();
            fetchMenus();
        };
        fetchApi({url, requestInfo}, onSuccess);
    }

    return (
        <Box sx={{ width: '100%', margin: '1%' }}>
            <h1>메뉴 관리</h1>
            <List>
                {
                    menus.map((menu, index) => (
                        <ListItem key={index}>
                            <ListItemText 
                                primary={menu.name} 
                                secondary={
                                    <React.Fragment>
                                        {menu.description}
                                        <IconButton size="small" onClick={() => handleDescriptionModalOpen(menu.id)}>
                                            <Edit fontSize="inherit" />
                                        </IconButton>
                                    </React.Fragment>
                                }/>
                            <Stack direction="row" spacing={2} justifyContent='center' alignItems='center'>
                                <Typography>{`${menu.price}원`}</Typography>
                                <FormControlLabel
                                    control={
                                        <Switch checked={menu.soldOut} color="error" onChange={(e) => handleSoldOutModalOpen(e.target.checked, menu.id)} />
                                    }
                                    label="품절"
                                    labelPlacement="top"
                                />
                            </Stack>
                        </ListItem>
                    ))
                }
            </List>
            <Modal open={isDescriptionModalOpen} onClose={handleDescriptionModalClose}>
                <Box className="modalContainer">
                    <Typography variant="h5" component="h2" className="modalTitle">
                        메뉴 설명 편집
                    </Typography>
                    <TextField
                        label="메뉴 설명(25자 이내)"
                        variant="outlined"
                        fullWidth={true}
                        value={descriptionInput}
                        onChange={(e) => setDescriptionInput(e.target.value)}
                        className="modalTextInput"/>

                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleSubmitDescription}
                        className="modalConfirmButton">
                        저장
                    </Button>
                </Box>
            </Modal>
            <Modal open={isSoldOutModalOpen} onClose={handleSoldOutModalClose}>
                <Box className="modalContainer" textAlign='center'>
                    <Typography variant="h5" component="h2" className="modalTitle">
                        {soldOutInput ? '품절 처리 하시겠습니까?' : '품절 해제 하시겠습니까?'}
                    </Typography>
                    <Stack direction="row" spacing={2} justifyContent='center' alignItems='center'>
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={handleSubmitSoldOut}
                            className="modalConfirmButton">
                            확인
                        </Button>
                        <Button
                            variant="outlined"
                            color="primary"
                            onClick={handleSoldOutModalClose}
                            className="modalConfirmButton">
                            취소
                        </Button>
                    </Stack>
                </Box>
            </Modal>
        </Box>
    );
}