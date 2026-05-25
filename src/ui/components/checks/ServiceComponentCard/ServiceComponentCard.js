import '../../vehicleComponents/ComponentCard/ComponentCard.css'
import Card from 'react-bootstrap/Card';
import {Alert} from "react-bootstrap";
import CarLogo from '../../../../images/car-logo.jpg'
import EngineOil from '../../../../images/engine-oil-logo.jpg'
import OilFilter from '../../../../images/oil-filter-logo.webp'
import AirFilter from '../../../../images/air-filter-logo.jpg'
import FuelFilter from '../../../../images/fuel-filter-logo.webp'
import SparkPlug from '../../../../images/spark-plug-logo.jpg'
import GlowPlug from '../../../../images/glow-plug-logo.jpg'
import OxygenSensor from '../../../../images/oxygen-sensor-logo.webp'
import CrankCaseBreather from '../../../../images/crankcase-breather-filter-logo.jpg'
import EngineBelt from '../../../../images/engine-belt-logo.jpg'
import TimingGear from '../../../../images/timing-gear-logo.jpg'
import EngineCoolant from '../../../../images/engine-coolant-logo.jpg'
import DPFFilter from '../../../../images/dpf-filter-logo.jpg'
import SCR from '../../../../images/scr-logo.jpg'
import CatalyticConverter from '../../../../images/catalytic-converter-logo.jpg'
import EGRValve from '../../../../images/egr-valve-logo.jpg'
import WaterPump from '../../../../images/water-pump-logo.jpg'
import Thermostat from '../../../../images/thermostat-logo.webp'
import Battery from '../../../../images/battery-logo.webp'
import Alternator from '../../../../images/alternator-logo.jpg'
import BrakePads from '../../../../images/brake-pads-logo.jpg'
import BrakeDiscs from '../../../../images/brake-discs-logo.jpg'
import BrakeFluid from '../../../../images/brake-fluid-logo.jpg'
import BrakeDrums from '../../../../images/brake-drum-logo.jpg'
import TransmissionFilter from '../../../../images/transmission-filter-logo.jpg'
import TransmissionOil from '../../../../images/transmission-oil-logo.png'
import Driveshaft from '../../../../images/driveshaft-logo.jpg'
import AuxiliaryFilter from '../../../../images/auxiliary-filter-logo.jpg'
import HVACFilter from '../../../../images/hvac-filter-logo.jpg'
import FrontForkOil from '../../../../images/front-fork-oil-logo.jpg'
import RearShockAbsorber from '../../../../images/rear-shock-absorber-logo.jpg'
import DriveChain from '../../../../images/drive-chain-logo.jpg'
import Sprockets from '../../../../images/sprockets-logo.webp'
import ThrottleCable from '../../../../images/throttle-cable-logo.webp'
import DriveOil from '../../../../images/drive-oil-logo.webp'
import ClutchCable from "../../../../images/clutch-cable-logo.jpg";
import Tires from "../../../../images/tires-logo.jpg";
import PollenFilter from "../../../../images/pollen-filter-logo.jpg";
import DriveBelt from "../../../../images/drive-belt-logo.webp";
import SteeringBearings from "../../../../images/steering-bearings-logo.jpg";
import SwingarmBearings from "../../../../images/swingarm-bearings-logo.jpg";
import ChainLube from "../../../../images/chain-lube-logo.jpg";
const ServiceComponentCard = ({componentDetail}) => {
    const componentTypeImageMap = {
        ENGINE_OIL: EngineOil,
        OIL_FILTER: OilFilter,
        AIR_FILTER: AirFilter,
        FUEL_FILTER: FuelFilter,
        SPARK_PLUG: SparkPlug,
        GLOW_PLUG: GlowPlug,
        CRANKCASE_BREAKER_FILTER: CrankCaseBreather,
        ENGINE_BELT: EngineBelt,
        TIMING_GEAR: TimingGear,
        ENGINE_COOLANT: EngineCoolant,
        COOLANT: EngineCoolant,
        OXYGEN_SENSOR: OxygenSensor,
        DPF_FILTER: DPFFilter,
        SCR: SCR,
        CATALYTIC_CONVERTER: CatalyticConverter,
        EGR_VALVE: EGRValve,
        WATER_PUMP: WaterPump,
        THERMOSTAT: Thermostat,
        BATTERY: Battery,
        ALTERNATOR: Alternator,
        BRAKE_PADS: BrakePads,
        BRAKE_DISCS: BrakeDiscs,
        BRAKE_FLUID: BrakeFluid,
        BRAKE_DRUMS: BrakeDrums,
        TRANSMISSION_FILTER: TransmissionFilter,
        TRANSMISSION_OIL: TransmissionOil,
        DRIVESHAFT: Driveshaft,
        AUXILIARY_FILTER: AuxiliaryFilter,
        HVAC_FILTER: HVACFilter,
        FRONT_FORK_OIL: FrontForkOil,
        REAR_SHOCK_ABSORBER: RearShockAbsorber,
        DRIVE_CHAIN: DriveChain,
        SPROCKETS: Sprockets,
        THROTTLE_CABLE: ThrottleCable,
        CLUTCH_CABLE: ClutchCable,
        DRIVE_OIL: DriveOil,
        TIRES: Tires,
        CABIN_AIR_FILTER: PollenFilter,
        DRIVE_BELT: DriveBelt,
        STEERING_HEAD_BEARINGS: SteeringBearings,
        SWINGARM_BEARINGS: SwingarmBearings,
        CHAIN_LUBRICATION: ChainLube
    };
    const conditionTypeMap = {
        VERY_GOOD: "Very Good",
        GOOD: "Good",
        POOR: "Poor",
        OOS: "Not Functional",
        UNKNOWN: "Unknown"
    }
    const getCondition = (conditionType) => {
        return conditionTypeMap[conditionType];
    }
    const getComponentImage = (componentType) => {
        return componentTypeImageMap[componentType] || CarLogo;
    };
    return (
        <Card border="secondary" className="w-100">
            <Card.Img variant="top componentImg" src={getComponentImage(componentDetail.component.type)} />
            <Card.Body>
                <Card.Title className="mb-3">{componentDetail.component.name}</Card.Title>
            </Card.Body>
            <Card.Footer className="text-center">
                {(() => {
                    if(componentDetail.currentCondition === "VERY_GOOD"){
                        return (
                            <>
                                <p className="mb-2 mt-2">Previous Condition: {getCondition(componentDetail.previousCondition)}</p>
                                <Alert className="border-dark mb-2" key="success" variant="success">
                                    <p className="mb-1">Updated Condition: {getCondition(componentDetail.currentCondition)}</p>
                                </Alert>
                            </>
                        );
                    }

                    if(componentDetail.currentCondition === "GOOD"){
                        return (
                            <>
                                <p className="mb-2 mt-2">Previous Condition: {getCondition(componentDetail.previousCondition)}</p>
                                <Alert className="border-dark mb-2" key="success" variant="primary">
                                    <p className="mb-1">Updated Condition: {getCondition(componentDetail.currentCondition)}</p>
                                </Alert>
                            </>
                        );
                    }

                    if(componentDetail.currentCondition === "POOR"){
                        return (
                            <>
                                <p className="mb-2 mt-2">Previous Condition: {getCondition(componentDetail.previousCondition)}</p>
                                <Alert className="border-dark mb-2" key="success" variant="warning">
                                    <p className="mb-1">Updated Condition: {getCondition(componentDetail.currentCondition)}</p>
                                </Alert>
                            </>
                        );
                    }

                    if(componentDetail.currentCondition === "OOS"){
                        return (
                            <>
                                <p className="mb-2 mt-2">Previous Condition: {getCondition(componentDetail.previousCondition)}</p>
                                <Alert className="border-dark mb-2" key="success" variant="danger">
                                    <p className="mb-1">Updated Condition: {getCondition(componentDetail.currentCondition)}</p>
                                </Alert>
                            </>
                        );
                    }

                    return (
                        <>
                            <p className="mb-2 mt-2">Previous Condition: {getCondition(componentDetail.previousCondition)}</p>
                            <Alert className="border-dark mb-2" key="success" variant="secondary">
                                <p className="mb-1">Updated Condition: {getCondition(componentDetail.currentCondition)}</p>
                            </Alert>
                        </>
                    );
                })()}
            </Card.Footer>
        </Card>
    )
}

export default ServiceComponentCard;