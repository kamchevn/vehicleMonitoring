import "./ConfirmEmail.css";
import {useEffect, useRef} from "react";
import {useNavigate, useSearchParams} from "react-router";
import {Card} from "react-bootstrap";
import Spinner from "react-bootstrap/Spinner";
import {useMediaQuery} from "../../../../hooks/useMediaQuery";

const ConfirmEmail = ({confirmEmail}) => {
    const isMobile = useMediaQuery("(max-width: 575px)");
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    const hasStarted = useRef(false);

    useEffect(() => {
        if (hasStarted.current) {
            return;
        }
        hasStarted.current = true;

        const confirm = async () => {
            const token = searchParams.get("token");

            if (!token) {
                navigate("/login", {
                    replace: true,
                    state: {
                        alertVariant: "danger",
                        alertMessage: "Confirmation token is missing or invalid.",
                    },
                });
                return;
            }

            try {
                await confirmEmail(token);
                navigate("/login", {
                    replace: true,
                    state: {
                        alertVariant: "success",
                        alertMessage: "Your email has been confirmed successfully. You can now log in.",
                    },
                });
            } catch (err) {
                const message =
                    err.response?.data?.message ||
                    "Confirmation token is invalid or has expired.";
                navigate("/login", {
                    replace: true,
                    state: {
                        alertVariant: "danger",
                        alertMessage: message,
                    },
                });
            }
        };

        confirm();
    }, [confirmEmail, navigate, searchParams]);

    return (
        <Card border="dark" className={isMobile ? "mobile" : "pc"}>
            <Card.Header>Confirming email</Card.Header>
            <Card.Body className="text-center py-5">
                <Spinner animation="grow" variant="dark" />
                <p className="mt-3 mb-0">Please wait while we confirm your email...</p>
            </Card.Body>
        </Card>
    );
};

export default ConfirmEmail;
