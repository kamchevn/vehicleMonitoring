import componentsRepository from "../repository/ComponentsRepository";

export const createComponentsFromTemplates = async (vehicle, templates) => {
    if (!vehicle?.id || !templates?.length) return;

    for (const template of templates) {
        const formData = new FormData();
        formData.append("componentTemplateId", template.id);
        formData.append("vehicleId", vehicle.id);
        formData.append("name", template.name);
        formData.append("measuringUnit", template.measuringUnit);
        formData.append("condition", "UNKNOWN");
        formData.append("counter","0");

        if (template.defaultValue != null) {
            formData.append("value", template.defaultValue);
        }

        await componentsRepository.create(formData);
    }
};