import '../ComponentCard/ComponentCard.css'
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
import ClutchCable from '../../../../images/clutch-cable-logo.jpg'
import DriveOil from '../../../../images/drive-oil-logo.webp'
import Tires from '../../../../images/tires-logo.jpg'
import PollenFilter from '../../../../images/pollen-filter-logo.jpg'
import DriveBelt from '../../../../images/drive-belt-logo.webp'
import SteeringBearings from '../../../../images/steering-bearings-logo.jpg'
import SwingarmBearings from '../../../../images/swingarm-bearings-logo.jpg'
import ChainLube from '../../../../images/chain-lube-logo.jpg'
import Clutch from '../../../../images/clutch-logo.png'
import AxleOil from '../../../../images/axle-oil-logo.webp'
import Button from "react-bootstrap/Button";
const ComponentCard = ({component, showCondition = true, isSelected = false, onToggleSelect}) => {
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
        CHAIN_LUBRICATION: ChainLube,
        CLUTCH: Clutch,
        AXLE_OIL: AxleOil
    };
    const getComponentImage = (componentType) => {
        return componentTypeImageMap[componentType] || CarLogo;
    };

    const formatLastChecked = (isoDateTime) => {
        if (!isoDateTime) return "Never";

        const now = new Date();
        const checked = new Date(isoDateTime);

        const startOfToday = new Date(now.getFullYear(), now.getMonth(), now.getDate());
        const startOfChecked = new Date(
            checked.getFullYear(),
            checked.getMonth(),
            checked.getDate()
        );

        const diffMs = startOfToday - startOfChecked;
        const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

        if (diffDays === 0) return "Today";
        if (diffDays === 1) return "1 day ago";
        if (diffDays >= 2 && diffDays <= 6) return `${diffDays} days ago`;

        if (diffDays >= 7 && diffDays < 14) return "1 week ago";
        if (diffDays >= 14 && diffDays < 28) {
            const weeks = Math.floor(diffDays / 7);
            return `${weeks} weeks ago`;
        }

        let months =
            (now.getFullYear() - checked.getFullYear()) * 12 +
            (now.getMonth() - checked.getMonth());

        let tempDate = new Date(checked);
        tempDate.setMonth(tempDate.getMonth() + months);

        if (tempDate > now) {
            months--;
            tempDate.setMonth(tempDate.getMonth() - 1);
        }

        const remainingDays = Math.floor(
            (startOfToday - tempDate) / (1000 * 60 * 60 * 24)
        );
        const weeks = Math.floor(remainingDays / 7);

        if (months === 1 && weeks === 0) return "1 month ago";
        if (months >= 2 && months <= 11 && weeks === 0)
            return `${months} months ago`;

        if (months >= 1 && months <= 11 && weeks > 0)
            return `${months} month${months > 1 ? "s" : ""}, ${weeks} week${weeks > 1 ? "s" : ""} ago`;

        const years = Math.floor(months / 12);
        const remainingMonths = months % 12;

        if (years === 1 && remainingMonths === 0) return "1 year ago";
        if (years >= 2 && remainingMonths === 0)
            return `${years} years ago`;

        return `${years} year${years > 1 ? "s" : ""}, ${remainingMonths} month${remainingMonths > 1 ? "s" : ""} ago`;
    };
    return (
        <Card border="secondary" className="w-100">
            <Card.Img variant="top componentImg" src={getComponentImage(component.type)} />
            <Card.Body>
                <Card.Title className="mb-3">{component.name}</Card.Title>
                <Card.Text>Last Checked: {formatLastChecked(component.lastChecked)}</Card.Text>
            </Card.Body>
            {showCondition ? (
                <Card.Footer className="text-center">
                    {(() => {
                        if(component.condition === "VERY_GOOD"){
                            return (
                                <Alert className="border-dark mb-2" key="success" variant="success">
                                    Condition: Very Good
                                </Alert>
                            );
                        }

                        if(component.condition === "GOOD"){
                            return (
                                <Alert className="border-dark mb-2" key="primary" variant="primary">
                                    Condition: Good
                                </Alert>
                            );
                        }

                        if(component.condition === "POOR"){
                            return (
                                <Alert className="border-dark mb-2" key="warning" variant="warning">
                                    Condition: Poor
                                </Alert>
                            );
                        }

                        if(component.condition === "OOS"){
                            return (
                                <Alert className="border-dark mb-2" key="danger" variant="danger">
                                    Condition: Not Functional
                                </Alert>
                            );
                        }

                        return (
                            <Alert className="border-dark mb-2" key="secondary" variant="secondary">
                                Condition: Unknown
                            </Alert>
                        );
                    })()}
                    {component.warning && (
                        <p className="mb-0">ℹ️ Check time coming soon!</p>
                    )}
                    {component.checkRequired && component.condition === "POOR" && (
                        <p className="mb-0">🔧️ Time for check!</p>
                    )}
                    {component.checkRequired && component.condition === "OOS" && (
                        <p className="mb-0">⚠️ Must check!</p>
                    )}
                </Card.Footer>
            ) : (
                <Card.Footer className="text-center">
                    <Button
                        variant={isSelected ? "danger" : "primary"}
                        onClick={onToggleSelect}
                    >
                        {isSelected ? "Deselect" : "Select"}
                    </Button>
                </Card.Footer>
            )}
        </Card>
    )
}

export default ComponentCard;