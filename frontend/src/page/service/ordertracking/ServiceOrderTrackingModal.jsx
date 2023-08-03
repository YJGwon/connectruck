import React, {useState} from 'react';
import {
    Box,
    Modal,
    Typography,
    TextField,
    Button,
    Fade,
    Paper,
    Stack,
    Accordion,
    AccordionSummary,
    AccordionDetails,
    Dialog,
    DialogTitle,
    DialogActions
} from '@mui/material';
import {ExpandMore} from '@mui/icons-material';

import {fetchData} from '../../../function/CustomFetch';

import {OrderDetailTable} from '../../../component/table/OrderDetailTable';

const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4
};

const OrderCancelDialog = ({isOpen, onClose}) => {
    return (
        <Dialog
            open={isOpen}
            onClose={() => onClose(false)}>
            <DialogTitle id="alert-dialog-title">
                정말 주문을 취소하시겠습니까?
            </DialogTitle>
            <DialogActions>
                <Button onClick={() => onClose(true)}>네, 취소할게요.</Button>
                <Button onClick={() => onClose(false)} autoFocus="autoFocus">돌아갈래요.</Button>
            </DialogActions>
        </Dialog>
    );
} 

const OrderDetailModal = ({orderDetail, onClose}) => {
    const [isDialogOpen, setIsDialogOpen] = useState(false);

    const handleCancel = () => {
        setIsDialogOpen(true);
    }

    const handleDialogClose = (isConfirmed) => {
        setIsDialogOpen(false);
        if (isConfirmed) {
            alert('취소되었습니다.');
            onClose();
        }
    }

    return (
        <Modal
            open={orderDetail !== null}
            onClose={onClose}
            closeAfterTransition={true}>
            <Fade in={orderDetail !== null}>
                <Box component={Paper} sx={{p: 1, maxHeight: '100%', overflow: 'auto'}}>
                    <h2>현재 {orderDetail?.status} 상태입니다.</h2>
                    <Accordion>
                        <AccordionSummary
                            expandIcon={<ExpandMore />}>
                            <h3>푸드트럭 정보</h3>
                        </AccordionSummary>
                        <AccordionDetails>
                          <Typography>이름: {orderDetail?.truck.name}</Typography>
                          <Typography>차량 번호: {orderDetail?.truck.carNumber}</Typography>
                        </AccordionDetails>
                    </Accordion>
                    <Accordion>
                        <AccordionSummary
                            expandIcon={<ExpandMore />}>
                            <h3>행사 정보</h3>
                        </AccordionSummary>
                        <AccordionDetails>
                            <Typography>이름: {orderDetail?.event.name}</Typography>
                            <Typography>위치: {orderDetail?.event.location}</Typography>
                        </AccordionDetails>
                    </Accordion>
                    <Accordion>
                        <AccordionSummary
                            expandIcon={<ExpandMore />}>
                            <h3>주문 상세</h3>
                        </AccordionSummary>
                        <AccordionDetails>
                            {orderDetail && <OrderDetailTable orderDetail={orderDetail}/>}
                        </AccordionDetails>
                    </Accordion>
                    <Stack
                        spacing={2}
                        direction="column"
                        sx={{
                            margin: 'auto',
                            width: '200px',
                            p: 2,
                            justifyContent: 'center'
                        }}>
                        <Button variant="outlined" onClick={onClose}>닫기</Button>
                        {orderDetail && orderDetail.status === '접수 대기' && <Button color='error' size='small' onClick={handleCancel}>주문 취소</Button>}
                    </Stack>
                    <OrderCancelDialog isOpen={isDialogOpen} onClose={handleDialogClose}/>
                </Box>
            </Fade>
        </Modal>
    );
}

export const ServiceOrderTrackingModal = ({open, onClose}) => {
    const [phone, setPhone] = useState('');
    const [orderId, setOrderId] = useState('');
    const [orderDetail, setOrderDetail] = useState(null);

    const handleSubmit = () => {
        fetchOrderDetail();
    }

    const handleModalClose = () => {
        setPhone('');
        setOrderId('');
        setOrderDetail(null);
    }

    const fetchOrderDetail = () => {
        const url = `${process.env.REACT_APP_API_URL}/api/orders/${orderId}`;
        const requestInfo = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({phone})
        };
        const onSuccess = (data) => {
            setOrderDetail(data);
        };

        fetchData({url, requestInfo}, onSuccess);
    };

    return (
        <Modal open={open} onClose={onClose}>
            <Box sx={style}>
                <Typography variant="h5" component="h2" className="modalTitle">
                    주문 조회
                </Typography>
                <TextField
                    label="휴대폰 번호"
                    variant="outlined"
                    fullWidth={true}
                    value={phone}
                    onChange={(e) => setPhone(e.target.value)}
                    className="modalTextInput"/>
                <TextField
                    label="주문 번호"
                    variant="outlined"
                    fullWidth={true}
                    value={orderId}
                    onChange={(e) => setOrderId(e.target.value)}
                    className="modalTextInput"/>

                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleSubmit}
                    disabled={!phone || !orderId}
                    className="modalConfirmButton">
                    조회하기
                </Button>
                <OrderDetailModal orderDetail={orderDetail} onClose={handleModalClose} />
            </Box>
        </Modal>

    );
};
