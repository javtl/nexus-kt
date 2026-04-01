package com.nexus.routes

import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.*

/**
 * NX-301: Nexus Dashboard (Mission Control)
 * UI minimalista con estilo Framer/Moderno y paleta de colores de Kotlin.
 */
fun Route.dashboardRouting() {
    get("/dashboard") {
        call.respondHtml {
            head {
                meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
                title { +"Nexus.kt | Mission Control" }
                // CDN de Tailwind para prototipado rápido de Hackathon
                script { src = "https://cdn.tailwindcss.com" }
                // Fuente moderna (Inter)
                link(href = "https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800;900&display=swap", rel = "stylesheet")
                style {
                    """
                    body { font-family: 'Inter', sans-serif; }
                    .framer-blob { 
                        position: absolute; 
                        filter: blur(100px); 
                        opacity: 0.5; 
                        z-index: -1; 
                    }
                    """
                }
            }
            body(classes = "bg-purple-950 text-purple-100 min-h-screen relative overflow-x-hidden") {

                // --- FRAMER-STYLE BACKGROUND GRADIENTS (BLOBS) ---
                div(classes = "framer-blob bg-purple-600 rounded-full w-96 h-96 -top-24 -left-24") {}
                div(classes = "framer-blob bg-magenta-500 rounded-full w-80 h-80 top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2") {}
                div(classes = "framer-blob bg-blue-600 rounded-full w-72 h-72 -bottom-24 -right-24") {}

                div(classes = "container mx-auto px-6 py-12 relative z-10") {

                    // --- HEADER (Framer Style: Floating & Blurred) ---
                    header(classes = "flex flex-col md:flex-row justify-between items-center mb-16 p-6 bg-purple-900/40 border border-purple-800/50 rounded-2xl shadow-lg backdrop-blur-xl gap-6") {
                        div {
                            h1(classes = "text-4xl font-extrabold tracking-tighter text-transparent bg-clip-text bg-gradient-to-r from-purple-400 to-magenta-400") {
                                +"NEXUS.KT"
                            }
                            p(classes = "text-purple-400 text-xs mt-1 uppercase tracking-widest font-semibold") { +"AI Action Bridge Framework" }
                        }
                        div(classes = "flex gap-3") {
                            statusBadge("MongoDB", "Online", "green")
                            statusBadge("Auth0", "Active", "purple")
                            statusBadge("Engine", "Ready", "magenta")
                        }
                    }

                    // --- STATS CARDS (Floating cards with subtle borders) ---
                    div(classes = "grid grid-cols-1 md:grid-cols-3 gap-8 mb-16") {
                        statCard("Active Profiles", "12", "text-purple-400", "Users in MongoDB Atlas")
                        statCard("Total Executions", "45", "text-magenta-400", "Successful AI Actions")
                        statCard("Security Alerts", "3", "text-orange-400", "MFA Step-up challenges")
                    }

                    // --- ACTIVITY FEED (Modern Table Design) ---
                    div(classes = "bg-purple-900/30 rounded-3xl border border-purple-800/50 shadow-2xl overflow-hidden backdrop-blur-lg") {
                        div(classes = "px-8 py-6 border-b border-purple-800/50 bg-purple-900/50 flex justify-between items-center") {
                            h2(classes = "text-xl font-bold text-purple-200") { +"Real-time Activity Feed" }
                            span(classes = "inline-flex items-center gap-2 px-3 py-1 text-xs text-magenta-400 bg-magenta-500/10 border border-magenta-500/20 rounded-full animate-pulse") {
                                div(classes = "w-1.5 h-1.5 rounded-full bg-magenta-500") {}
                                +"Live monitoring"
                            }
                        }

                        div(classes = "overflow-x-auto") {
                            table(classes = "w-full text-left border-collapse") {
                                thead(classes = "bg-purple-950/70 text-purple-400 text-xs uppercase tracking-wider") {
                                    tr {
                                        th(classes = "px-8 py-5 font-semibold") { +"Action Type" }
                                        th(classes = "px-8 py-5 font-semibold") { +"Target Resource" }
                                        th(classes = "px-8 py-5 font-semibold text-center") { +"Status" }
                                        th(classes = "px-8 py-5 font-semibold text-right") { +"Timestamp" }
                                    }
                                }
                                tbody(classes = "divide-y divide-purple-800/30") {
                                    // Datos Mock
                                    activityRow("MARK_COMPLETED", "steam:app_2077", "Success", "Just now")
                                    activityRow("EXECUTE_TRANSFER", "vault:primary", "Step-up Req", "12m ago")
                                    activityRow("UPDATE_PROFILE", "gamer:nexus_01", "Success", "1h ago")
                                    activityRow("REVOKE_ACCESS", "api:discord_bridge", "Denied", "3h ago")
                                }
                            }
                        }
                    }

                    // --- FOOTER (Subtle & Minimal) ---
                    footer(classes = "mt-16 text-center text-purple-600 text-xs") {
                        +"Nexus.kt Framework v1.0.0-rc1 | Built for Hackathon 2026"
                    }
                }
            }
        }
    }
}

// --- UI COMPONENTS (HELPERS) ---

fun FlowContent.statusBadge(label: String, status: String, color: String) {
    div(classes = "flex items-center gap-2 px-3 py-1.5 bg-$color-500/10 border border-$color-500/30 rounded-full") {
        div(classes = "w-2 h-2 rounded-full bg-$color-500 shadow-[0_0_8px_rgba($color,0.6)] animate-pulse") {}
        span(classes = "text-[10px] font-bold text-$color-300 uppercase tracking-tight") { +"$label: $status" }
    }
}

fun FlowContent.statCard(title: String, value: String, colorClass: String, subtitle: String) {
    div(classes = "bg-purple-900/40 p-10 rounded-3xl border border-purple-800/50 hover:border-purple-600 transition-all shadow-xl backdrop-blur-sm group") {
        p(classes = "text-purple-400 text-sm font-medium mb-2 group-hover:text-purple-300") { +title }
        h3(classes = "text-5xl font-black mb-3 tracking-tighter $colorClass") { +value }
        p(classes = "text-purple-600 text-xs italic") { +subtitle }
    }
}

fun TBODY.activityRow(action: String, target: String, status: String, time: String) {
    tr(classes = "hover:bg-purple-800/40 transition-colors group") {
        td(classes = "px-8 py-5") {
            span(classes = "font-mono text-xs text-magenta-300 bg-magenta-500/10 px-3 py-1 rounded border border-magenta-500/20") { +action }
        }
        td(classes = "px-8 py-5 text-sm text-purple-200 font-medium") { +target }
        td(classes = "px-8 py-5 text-center") {
            val (badgeClass, dotClass) = when {
                status.contains("Success") -> "text-green-300 bg-green-400/10 border-green-500/20" to "bg-green-500"
                status.contains("Step-up") -> "text-orange-300 bg-orange-400/10 border-orange-500/20" to "bg-orange-500"
                else -> "text-magenta-300 bg-magenta-500/10 border-magenta-500/20" to "bg-magenta-500"
            }
            span(classes = "inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-[10px] font-bold border $badgeClass") {
                div(classes = "w-1.5 h-1.5 rounded-full $dotClass") {}
                +status.uppercase()
            }
        }
        td(classes = "px-8 py-5 text-right text-xs text-purple-600 font-mono") { +time }
    }
}