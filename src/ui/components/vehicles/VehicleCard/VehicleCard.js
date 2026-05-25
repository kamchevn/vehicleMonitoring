import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import DeleteVehicleModal from "../DeleteVehicleModal/DeleteVehicleModal";
import {useNavigate} from "react-router";
import {Alert} from "react-bootstrap";
import CarLogo from '../../../../images/car-logo.jpg'
import BusLogo from '../../../../images/bus-logo.jpg'
import TruckLogo from '../../../../images/truck-logo.jpg'
import MotorLogo from '../../../../images/motor-logo.jpg'
import InsertIntervalModal from "../InsertIntervalModal/InsertIntervalModal";
import {isLoggedIn,getUser} from "../../../../service/AuthService";
const VehicleCard = ({vehicle, onDelete, insertIntervalForVehicle}) => {
    const navigate = useNavigate();

    const editForm = (vehicle) => {
        navigate(`/vehicles/edit/${vehicle.id}`);
    };

    const infoRedirect = (vehicle) => {
        navigate(`/vehicles/${vehicle.id}`);
    }

    const regularServiceForm = (vehicle) => {
        navigate(`/vehicles/service/REGULAR/${vehicle.id}`);
    }

    const unknownServiceForm = (vehicle) => {
        navigate(`/vehicles/service/URGENT/${vehicle.id}`);
    }

    const status = vehicle.insertWindowStatus;

    if (!status) {
        return null; // or loading spinner
    }

    return (
        <Card border="secondary" className="w-100">
            {vehicle.type==="CAR" && (
                <Card.Img variant="top" src={CarLogo} />
            )}
            {vehicle.type==="BUS" && (
                <Card.Img variant="top" src={BusLogo} />
            )}
            {vehicle.type==="TRUCK" && (
                <Card.Img variant="top" src={TruckLogo} style={{padding: 15 + 'px'}} />
            )}
            {vehicle.type==="MOTORCYCLE" && (
                <Card.Img variant="top" src={MotorLogo} style={{transform: "scaleX(-1)"}} />
            )}
            <Card.Body>
                <Card.Title className="mb-3">{vehicle.name}</Card.Title>
                <Card.Text>Year: {vehicle.year}</Card.Text>
                {isLoggedIn() && getUser().roles?.includes("ROLE_ADMIN") && (
                    <Card.Text>Created By: {vehicle.username}</Card.Text>
                )}
                {vehicle.insertPeriodType === "MONTHLY" && (
                    <Card.Text>Insert Period: <span style={{color: "#0a54b9"}}>Monthly</span></Card.Text>
                )}
                {vehicle.insertPeriodType === "WEEKLY" && (
                    <Card.Text>Insert Period: <span style={{color: "#146e43"}}>Weekly</span></Card.Text>
                )}
                <Button variant="info" className="me-2" onClick={() => infoRedirect(vehicle)}>ℹ️Info</Button>
                <Button variant="warning" className="me-2" onClick={() => editForm(vehicle)}>✏ Edit</Button>
                <DeleteVehicleModal vehicle={vehicle} onDelete={onDelete}/>
            </Card.Body>
                <Card.Footer className="text-center">
                    {(() => {
                        if(vehicle.condition === "VERY_GOOD"){
                            return (
                                <Alert className="border-dark mb-2" key="success" variant="success">
                                    Condition: Very Good
                                </Alert>
                            );
                        }

                        if(vehicle.condition === "GOOD"){
                            if(vehicle.needsCheck){
                                return (
                                    <Alert className="border-dark mb-2" key="primary" variant="primary">
                                        Condition: Good
                                        <p className="mt-1 mb-0">
                                            ⚠️ Check required!
                                        </p>
                                    </Alert>
                                )
                            }
                            return (
                                <Alert className="border-dark mb-2" key="primary" variant="primary">
                                    Condition: Good
                                </Alert>
                            );
                        }

                        if(vehicle.condition === "POOR"){
                            if(vehicle.needsCheck){
                                return (
                                    <Alert className="border-dark mb-2" key="warning" variant="warning">
                                        Condition: Poor
                                        <p className="mt-1 mb-0">
                                            ⚠️ Check required!
                                        </p>
                                    </Alert>
                                )
                            }
                            return (
                                <Alert className="border-dark mb-2" key="warning" variant="warning">
                                    Condition: Poor
                                </Alert>
                            );
                        }

                        if(vehicle.condition === "OOS"){
                            return (
                                <Alert className="border-dark mb-2" key="danger" variant="danger">
                                    Condition: Out Of Service
                                    <p className="mt-1 mb-0">
                                        ⚠️ Check required!
                                    </p>
                                </Alert>
                            );
                        }

                        return (
                            <Alert className="border-dark mb-2" key="secondary" variant="secondary">
                                Condition: Unknown
                                <p className="mt-1 mb-0">
                                    ⚠️ Check required!
                                </p>
                            </Alert>
                        );
                    })()}
                    {status.isPenaltyActive && (
                        <Alert variant="danger" className="border-dark mb-2">
                            🚫 Penalty active
                            <p className="mt-1 mb-0">
                                Insert window is restricted due to repeated late inserts.
                            </p>
                        </Alert>
                    )}
                    {vehicle.condition !== "UNKNOWN" && status.canInsertKilometers && (
                        <InsertIntervalModal vehicle={vehicle} unit="KILOMETERS" insertIntervalForVehicle={insertIntervalForVehicle}/>
                    )}
                    {vehicle.condition !== "UNKNOWN" && status.canInsertFuel && (
                        <InsertIntervalModal vehicle={vehicle} unit="BURNT_FUEL" insertIntervalForVehicle={insertIntervalForVehicle}/>
                    )}

                    {vehicle.condition !== "UNKNOWN" && status.hasCompletedAllInserts && (
                        <Button className="mb-2" variant="outline-success" disabled>
                            ✅ Done inserting for this week
                        </Button>
                    )}

                    {vehicle.condition !== "UNKNOWN" && status.isInsertUpcoming && (
                        <Button className="mb-2" variant="outline-info" disabled>
                            ℹ Insert upcoming!
                        </Button>
                    )}

                    {vehicle.condition === "UNKNOWN" && (
                        <Button variant="outline-secondary" onClick={() => unknownServiceForm(vehicle)}>
                            🧑‍🔧️ Check components
                        </Button>
                    )}
                    {vehicle.needsCheck && (
                        <>
                            <p className="mb-1">
                                <Button variant="outline-secondary" onClick={() => regularServiceForm(vehicle)}>
                                    🧑‍🔧️ Regular service
                                </Button>
                            </p>
                        </>
                    )}
                </Card.Footer>
        </Card>
    )
}

export default VehicleCard;