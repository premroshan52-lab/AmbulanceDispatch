let currentUsername = null;
let currentRole = null;


/* =========================================================
   CURRENT USER
   ========================================================= */

async function loadCurrentUser() {

    try {

        const response =
            await fetch(
                "/auth/me",
                {
                    credentials: "include"
                }
            );

        if (
            response.status === 401 ||
            response.status === 403
        ) {

            window.location.href =
                "/login.html";

            return false;
        }

        if (!response.ok) {

            throw new Error(
                "Unable to verify login"
            );
        }

        const data =
            await response.json();

        currentUsername =
            data.username;

        currentRole =
            data.role;

        document.getElementById(
            "currentUsername"
        ).textContent =
            currentUsername;

        document.getElementById(
            "currentRole"
        ).textContent =
            currentRole;

        applyRolePermissions();

        return true;

    } catch (error) {

        console.error(error);

        window.location.href =
            "/login.html";

        return false;
    }
}


/* =========================================================
   ROLE PERMISSIONS
   ========================================================= */

function applyRolePermissions() {

    const dispatchPanel =
        document.getElementById(
            "dispatchPanel"
        );

    const canDispatch =
        currentRole === "ADMIN" ||
        currentRole === "DISPATCHER";

    if (!canDispatch) {

        dispatchPanel.classList.add(
            "role-hidden"
        );

    } else {

        dispatchPanel.classList.remove(
            "role-hidden"
        );
    }
}


/* =========================================================
   DASHBOARD
   ========================================================= */

async function loadDashboard() {

    try {

        const loggedIn =
            await loadCurrentUser();

        if (!loggedIn) {
            return;
        }

        const response =
            await fetch(
                "/analytics/dashboard",
                {
                    credentials: "include"
                }
            );

        if (
            response.status === 401 ||
            response.status === 403
        ) {

            window.location.href =
                "/login.html";

            return;
        }

        if (!response.ok) {

            throw new Error(
                "Unable to load dashboard data"
            );
        }

        const data =
            await response.json();

        updateDashboard(data);

        await loadZones();

        await loadAmbulances();

        await loadEmergencyCalls();

    } catch (error) {

        console.error(error);

        showDispatchMessage(
            "Unable to load dashboard. Make sure Spring Boot is running.",
            false
        );
    }
}


/* =========================================================
   DASHBOARD DATA
   ========================================================= */

function updateDashboard(data) {

    document.getElementById(
        "totalAmbulances"
    ).textContent =
        data.totalAmbulances;

    document.getElementById(
        "availableAmbulances"
    ).textContent =
        data.availableAmbulances;

    document.getElementById(
        "busyAmbulances"
    ).textContent =
        data.busyAmbulances;

    document.getElementById(
        "totalEmergencyCalls"
    ).textContent =
        data.totalEmergencyCalls;

    document.getElementById(
        "completedCalls"
    ).textContent =
        data.completedCalls;

    document.getElementById(
        "activeCalls"
    ).textContent =
        data.activeCalls;

    document.getElementById(
        "averageResponse"
    ).textContent =
        formatMinutes(
            data.averageResponseTimeMinutes
        );

    document.getElementById(
        "fastestResponse"
    ).textContent =
        formatMinutes(
            data.fastestResponseTimeMinutes
        );

    document.getElementById(
        "slowestResponse"
    ).textContent =
        formatMinutes(
            data.slowestResponseTimeMinutes
        );
}


/* =========================================================
   FORMAT RESPONSE TIME
   ========================================================= */

function formatMinutes(minutes) {

    if (
        minutes === null ||
        minutes === undefined ||
        isNaN(Number(minutes))
    ) {

        return "--";
    }

    const totalSeconds =
        Math.round(
            Number(minutes) * 60
        );

    const mins =
        Math.floor(
            totalSeconds / 60
        );

    const secs =
        totalSeconds % 60;

    return (
        mins +
        " min " +
        String(secs).padStart(2, "0") +
        " sec"
    );
}


/* =========================================================
   LOAD ZONES
   ========================================================= */

async function loadZones() {

    try {

        const response =
            await fetch(
                "/zones",
                {
                    credentials: "include"
                }
            );

        if (
            response.status === 401 ||
            response.status === 403
        ) {

            window.location.href =
                "/login.html";

            return;
        }

        if (!response.ok) {

            throw new Error(
                "Unable to load zones"
            );
        }

        const zones =
            await response.json();

        populateZoneSelect(zones);

    } catch (error) {

        console.error(error);
    }


    try {

        const response =
            await fetch(
                "/analytics/zone-response-time",
                {
                    credentials: "include"
                }
            );

        if (!response.ok) {

            throw new Error(
                "Unable to load zone analytics"
            );
        }

        const analytics =
            await response.json();

        displayZones(analytics);

    } catch (error) {

        console.error(error);
    }
}


/* =========================================================
   ZONE DROPDOWN
   ========================================================= */

function populateZoneSelect(zones) {

    const select =
        document.getElementById(
            "zoneSelect"
        );

    select.innerHTML = `
        <option value="">
            Select emergency zone
        </option>
    `;

    zones.forEach(
        zone => {

            const option =
                document.createElement(
                    "option"
                );

            option.value =
                zone.id;

            option.textContent =
                zone.name;

            select.appendChild(
                option
            );
        }
    );
}


/* =========================================================
   CREATE EMERGENCY
   ========================================================= */

async function createEmergency() {

    if (
        currentRole !== "ADMIN" &&
        currentRole !== "DISPATCHER"
    ) {

        showDispatchMessage(
            "You do not have permission to create an emergency dispatch.",
            false
        );

        return;
    }

    const zoneSelect =
        document.getElementById(
            "zoneSelect"
        );

    const zoneId =
        zoneSelect.value;

    if (!zoneId) {

        showDispatchMessage(
            "Please select an emergency zone.",
            false
        );

        return;
    }

    try {

        const response =
            await fetch(
                "/emergency-calls",
                {
                    method: "POST",

                    credentials: "include",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body: JSON.stringify({
                        zoneId:
                            Number(zoneId)
                    })
                }
            );

        const data =
            await response.json();

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Unable to create emergency"
            );
        }

        const ambulanceNumber =
            data.ambulance
                ? data.ambulance.ambulanceNumber
                : "Unknown";

        const zoneName =
            data.callerZone
                ? data.callerZone.name
                : "Unknown";

        showDispatchMessage(
            `
            <strong>
                🚨 Emergency Created Successfully
            </strong>

            <br><br>

            Emergency ID:
            <strong>${data.id}</strong>

            <br>

            Emergency Zone:
            <strong>${zoneName}</strong>

            <br>

            Assigned Ambulance:
            <strong>${ambulanceNumber}</strong>

            <br>

            Status:
            <strong>${data.status}</strong>
            `,
            true
        );

        zoneSelect.value = "";

        await loadDashboard();

    } catch (error) {

        console.error(error);

        showDispatchMessage(
            error.message,
            false
        );
    }
}


/* =========================================================
   DISPATCH MESSAGE
   ========================================================= */

function showDispatchMessage(
    message,
    success
) {

    const result =
        document.getElementById(
            "dispatchResult"
        );

    result.classList.remove(
        "hidden",
        "success",
        "error"
    );

    if (success) {

        result.classList.add(
            "success"
        );

    } else {

        result.classList.add(
            "error"
        );
    }

    result.innerHTML =
        message;
}


/* =========================================================
   LOAD EMERGENCY CALLS
   ========================================================= */

async function loadEmergencyCalls() {

    try {

        const response =
            await fetch(
                "/emergency-calls",
                {
                    credentials: "include"
                }
            );

        if (
            response.status === 401 ||
            response.status === 403
        ) {

            window.location.href =
                "/login.html";

            return;
        }

        if (!response.ok) {

            throw new Error(
                "Unable to load emergency calls"
            );
        }

        const calls =
            await response.json();

        displayEmergencyCalls(
            calls
        );

    } catch (error) {

        console.error(error);
    }
}


/* =========================================================
   DISPLAY EMERGENCY CALLS
   ========================================================= */

function displayEmergencyCalls(calls) {

    const table =
        document.getElementById(
            "emergencyTable"
        );

    table.innerHTML = "";

    if (calls.length === 0) {

        table.innerHTML = `
            <tr>
                <td colspan="7">
                    No emergency calls found.
                </td>
            </tr>
        `;

        return;
    }

    calls
        .sort(
            (a, b) =>
                b.id - a.id
        )
        .forEach(
            call => {

                const row =
                    document.createElement(
                        "tr"
                    );

                const zoneName =
                    call.callerZone
                        ? call.callerZone.name
                        : "Unknown";

                const ambulanceNumber =
                    call.ambulance
                        ? call.ambulance.ambulanceNumber
                        : "Not assigned";

                /*
                 * RESPONSE TIME
                 *
                 * Converts backend minutes into:
                 * 13 min 00 sec
                 * 27 min 30 sec
                 */

                const responseTime =
                    call.responseTimeMinutes
                        !== null &&
                    call.responseTimeMinutes
                        !== undefined
                        ? formatMinutes(
                            call.responseTimeMinutes
                        )
                        : "--";

                let action = "";

                const canOperate =
                    currentRole === "ADMIN" ||
                    currentRole === "DISPATCHER";


                if (
                    call.status === "ASSIGNED" &&
                    canOperate
                ) {

                    action = `
                        <button
                            class="action-button arrive-button"
                            onclick="markArrived(${call.id})">
                            Arrive
                        </button>
                    `;
                }


                if (
                    call.status === "ARRIVED" &&
                    canOperate
                ) {

                    action = `
                        <button
                            class="action-button complete-button"
                            onclick="completeEmergency(${call.id})">
                            Complete
                        </button>
                    `;
                }


                if (
                    call.status === "COMPLETED"
                ) {

                    action = `
                        <span>
                            ✓ Completed
                        </span>
                    `;
                }


                if (
                    !canOperate &&
                    call.status !== "COMPLETED"
                ) {

                    action = `
                        <span>
                            View only
                        </span>
                    `;
                }


                const statusClass =
                    call.status
                        .toLowerCase();


                row.innerHTML = `
                    <td>${call.id}</td>

                    <td>${zoneName}</td>

                    <td>
                        <strong>
                            ${ambulanceNumber}
                        </strong>
                    </td>

                    <td>
                        ${formatDateTime(
                            call.callTime
                        )}
                    </td>

                    <td>

                        <span
                            class="status ${statusClass}">

                            ${call.status}

                        </span>

                    </td>

                    <td>
                        ${responseTime}
                    </td>

                    <td>
                        ${action}
                    </td>
                `;

                table.appendChild(
                    row
                );
            }
        );
}


/* =========================================================
   MARK ARRIVED
   ========================================================= */

async function markArrived(id) {

    if (
        currentRole !== "ADMIN" &&
        currentRole !== "DISPATCHER"
    ) {

        return;
    }

    try {

        const response =
            await fetch(
                `/emergency-calls/${id}/arrive`,
                {
                    method: "PUT",
                    credentials: "include"
                }
            );

        const data =
            await response.json();

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Unable to mark ambulance as arrived"
            );
        }

        showDispatchMessage(
            "🚑 Ambulance marked as ARRIVED.",
            true
        );

        await loadDashboard();

    } catch (error) {

        console.error(error);

        showDispatchMessage(
            error.message,
            false
        );
    }
}


/* =========================================================
   COMPLETE EMERGENCY
   ========================================================= */

async function completeEmergency(id) {

    if (
        currentRole !== "ADMIN" &&
        currentRole !== "DISPATCHER"
    ) {

        return;
    }

    try {

        const response =
            await fetch(
                `/emergency-calls/${id}/complete`,
                {
                    method: "PUT",
                    credentials: "include"
                }
            );

        const data =
            await response.json();

        if (!response.ok) {

            throw new Error(
                data.message ||
                data.error ||
                "Unable to complete emergency"
            );
        }

        showDispatchMessage(
            `
            <strong>
                ✅ Emergency Completed
            </strong>

            <br><br>

            Ambulance is now available again.
            `,
            true
        );

        await loadDashboard();

    } catch (error) {

        console.error(error);

        showDispatchMessage(
            error.message,
            false
        );
    }
}


/* =========================================================
   FORMAT DATE AND TIME
   ========================================================= */

function formatDateTime(dateTime) {

    if (!dateTime) {
        return "-";
    }

    const date =
        new Date(dateTime);

    if (isNaN(date.getTime())) {
        return "-";
    }

    return date.toLocaleString(
        "en-IN",
        {
            day: "2-digit",
            month: "2-digit",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit",
            second: "2-digit",
            hour12: true
        }
    );
}


/* =========================================================
   LOAD AMBULANCES
   ========================================================= */

async function loadAmbulances() {

    try {

        const response =
            await fetch(
                "/analytics/ambulances",
                {
                    credentials: "include"
                }
            );

        if (!response.ok) {

            throw new Error(
                "Unable to load ambulance data"
            );
        }

        const ambulances =
            await response.json();

        displayAmbulances(
            ambulances
        );

    } catch (error) {

        console.error(error);
    }
}


/* =========================================================
   DISPLAY AMBULANCES
   ========================================================= */

function displayAmbulances(
    ambulances
) {

    const table =
        document.getElementById(
            "ambulanceTable"
        );

    table.innerHTML = "";

    if (ambulances.length === 0) {

        table.innerHTML = `
            <tr>
                <td colspan="5">
                    No ambulances found.
                </td>
            </tr>
        `;

        return;
    }

    ambulances.forEach(
        ambulance => {

            const row =
                document.createElement(
                    "tr"
                );

            const statusClass =
                ambulance.status
                    .toLowerCase();

            row.innerHTML = `
                <td>
                    ${ambulance.ambulanceId}
                </td>

                <td>
                    <strong>
                        ${ambulance.ambulanceNumber}
                    </strong>
                </td>

                <td>
                    ${ambulance.zoneName}
                </td>

                <td>

                    <span
                        class="status ${statusClass}">

                        ${ambulance.status}

                    </span>

                </td>

                <td>
                    ${ambulance.emergencyCallsHandled}
                </td>
            `;

            table.appendChild(
                row
            );
        }
    );
}


/* =========================================================
   DISPLAY ZONE ANALYTICS
   ========================================================= */

function displayZones(zones) {

    const table =
        document.getElementById(
            "zoneTable"
        );

    table.innerHTML = "";

    if (zones.length === 0) {

        table.innerHTML = `
            <tr>
                <td colspan="5">
                    No zone data found.
                </td>
            </tr>
        `;

        return;
    }

    zones.forEach(
        zone => {

            const row =
                document.createElement(
                    "tr"
                );

            row.innerHTML = `
                <td>
                    <strong>
                        ${zone.zoneName}
                    </strong>
                </td>

                <td>
                    ${zone.totalCalls}
                </td>

                <td>
                    ${zone.completedCalls}
                </td>

                <td>
                    ${zone.activeCalls}
                </td>

                <td>
                    ${formatMinutes(
                        zone.averageResponseTimeMinutes
                    )}
                </td>
            `;

            table.appendChild(
                row
            );
        }
    );
}


/* =========================================================
   LOGOUT
   ========================================================= */

async function logout() {

    try {

        await fetch(
            "/auth/logout",
            {
                method: "POST",
                credentials: "include"
            }
        );

    } catch (error) {

        console.error(error);

    } finally {

        window.location.href =
            "/login.html";
    }
}


/* =========================================================
   PAGE LOAD
   ========================================================= */

document.addEventListener(
    "DOMContentLoaded",
    loadDashboard
);