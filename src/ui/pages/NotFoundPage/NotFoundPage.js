import {Alert} from "react-bootstrap";
import Container from "react-bootstrap/Container";
import NotFound from "../../../images/404-not-found-logo.png"
import {useMediaQuery} from "../../../hooks/useMediaQuery";
import {useNavigate} from "react-router";
import Button from "react-bootstrap/Button";
const NotFoundPage = () => {
    const isMobile = useMediaQuery("(max-width: 575px)");
    const navigate = useNavigate();
    const redirectHome = () => {
        navigate("/");
    }
    return (
        <Container>
            <Alert variant="warning">
                {!isMobile && (
                    <div style={{ display: "flex"}}>
                        <img src={NotFound} alt="" style={{ width: 20 + "%", height: 20 + "%" }} />
                        <div className="ms-4 content-div">
                            <Alert.Heading className="mb-3">Page not found</Alert.Heading>
                            <p>
                                You tried to access a certain url, which doesn't have anything to show.
                                Just return back to the home page by pressing the button below.
                            </p>
                            <Button variant="warning" onClick={redirectHome}>← Back to home</Button>
                        </div>
                    </div>
                )}
                {isMobile && (
                    <div>
                        <img src={NotFound} alt="" style={{ width: 50 + "%", height: 50 + "%", display: "block", margin: "auto" }} />
                        <div>
                            <Alert.Heading className="mt-2 mb-2">Page not found</Alert.Heading>
                            <p>
                                You tried to access a certain url, which doesn't have anything to show.
                                Just return back to the home page by pressing the button below.
                            </p>
                            <Button variant="warning" onClick={redirectHome}>← Back to home</Button>
                        </div>
                    </div>
                )}
            </Alert>
        </Container>
    )
}

export default NotFoundPage;