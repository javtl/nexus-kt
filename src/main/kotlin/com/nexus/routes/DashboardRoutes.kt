package com.nexus.routes

import com.nexus.repository.GamerProfileRepository
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.html.*

fun Route.dashboardRouting(gamerRepo: GamerProfileRepository) {
    get("/dashboard") {

        // 1. Fetching all profiles for the dynamic list
        val profiles = try {
            gamerRepo.getAllProfiles() // Make sure this method exists in your Repo
        } catch (e: Exception) {
            System.err.println("🔴 Mongo Connection Error: ${e.message}")
            emptyList()
        }

        val totalProfiles = profiles.size.toLong()

        call.respondHtml {
            head {
                meta(name = "viewport", content = "width=device-width, initial-scale=1.0")
                title { +"Nexus.kt | Mission Control" }
                link(href = "https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap", rel = "stylesheet")
                script { src = "https://cdn.tailwindcss.com" }
                script {
                    unsafe {
                        +"""
                        tailwind.config = {
                            theme: {
                                extend: {
                                    fontFamily: {
                                        sans: ['Inter', 'sans-serif'],
                                    }
                                }
                            }
                        }
                        """.trimIndent()
                    }
                }
            }

            body(classes = "bg-[#13111C] text-slate-200 min-h-screen relative overflow-x-hidden font-sans") {

                div(classes = "fixed inset-0 bg-[radial-gradient(circle_at_50%_-20%,rgba(127,82,255,0.3),transparent_50%)]") {}
                div(classes = "fixed inset-0 bg-[radial-gradient(circle_at_0%_100%,rgba(169,123,255,0.15),transparent_40%)]") {}
                div(classes = "fixed top-[-10%] left-[-10%] w-[600px] h-[600px] bg-purple-500/20 rounded-full blur-[120px] pointer-events-none") {}
                div(classes = "fixed bottom-[-10%] right-[-10%] w-[500px] h-[500px] bg-indigo-500/20 rounded-full blur-[120px] pointer-events-none") {}

                div(classes = "container mx-auto px-6 py-12 relative z-10") {

                    header(classes = "flex items-center justify-between mb-16") {
                        div(classes = "flex items-center gap-4") {
                            div(classes = "w-10 h-10 bg-gradient-to-br from-[#7F52FF] to-[#C711E1] rounded-xl shadow-[0_0_25px_rgba(127,82,255,0.5)] flex items-center justify-center") {
                                span(classes = "text-white font-black text-xl") { +"N" }
                            }
                            h1(classes = "text-3xl font-extrabold tracking-tighter text-white drop-shadow-sm") {
                                +"NEXUS"
                                span(classes = "text-[#A97BFF]") { +".KT" }
                            }
                        }
                        div(classes = "flex gap-4") {
                            statusBadge("DATABASE", "CONNECTED", "#4ade80")
                            statusBadge("AUTH0", "ACTIVE", "#A97BFF")
                        }
                    }

                    div(classes = "grid grid-cols-1 md:grid-cols-12 gap-6") {

                        framerCard("md:col-span-8 p-10") {
                            glowLayer()
                            cardHeader("TOTAL GAMER PROFILES")
                            h3(classes = "text-7xl font-black text-white mt-4 tracking-tighter") {
                                +"$totalProfiles"
                            }
                            p(classes = "text-slate-400 text-lg mt-2 font-medium") {
                                +"Synced via MongoDB Atlas"
                            }
                        }

                        framerCard("md:col-span-4 p-8 flex flex-col justify-between") {
                            glowLayer()
                            div {
                                cardHeader("ENGINE STATUS")
                                h3(classes = "text-4xl font-bold text-[#A97BFF] mt-2") { +"READY" }
                            }
                            div(classes = "flex items-center gap-2 text-sm text-slate-400 animate-pulse") {
                                div(classes = "w-2 h-2 bg-[#4ade80] rounded-full") {}
                                +"Listening for AI actions..."
                            }
                        }

                        // 📋 DYNAMIC GAMER TABLE
                        framerCard("md:col-span-12 overflow-hidden") {
                            div(classes = "px-8 py-6 border-b border-white/10 bg-white/5") {
                                h2(classes = "text-xl font-bold text-white") { +"Live Synchronized Profiles" }
                            }
                            div(classes = "overflow-x-auto") {
                                table(classes = "w-full text-left") {
                                    thead(classes = "bg-white/5 text-slate-400 text-xs uppercase tracking-widest") {
                                        tr {
                                            th(classes = "px-8 py-4") { +"Gamer Username" }
                                            th(classes = "px-8 py-4") { +"Email Address" }
                                            th(classes = "px-8 py-4 text-center") { +"Level" }
                                            th(classes = "px-8 py-4 text-right") { +"Auth0 ID" }
                                        }
                                    }
                                    tbody(classes = "divide-y divide-white/5") {
                                        if (profiles.isEmpty()) {
                                            tr {
                                                td(classes = "px-8 py-10 text-slate-500 italic") {
                                                    colSpan = "4"
                                                    +"No profiles synchronized. Please send a POST request via Postman."
                                                }
                                            }
                                        } else {
                                            profiles.forEach { profile ->
                                                gamerRow(profile.username, profile.email, profile.level, profile.id)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    footer(classes = "mt-20 text-center text-slate-500 text-xs tracking-widest uppercase") {
                        +"Nexus.kt Architecture • Hackathon 2026"
                    }
                }
            }
        }
    }
}

// 🧩 RE-USABLE COMPONENTS

fun TBODY.gamerRow(name: String, email: String, level: Int, id: String) {
    tr(classes = "group hover:bg-white/[0.02] transition-colors") {
        td(classes = "px-8 py-5") {
            span(classes = "font-semibold text-white") { +name }
        }
        td(classes = "px-8 py-5 text-slate-400 text-sm") {
            +email
        }
        td(classes = "px-8 py-5 text-center") {
            span(classes = "text-xs font-bold px-3 py-1 rounded-full border border-[#A97BFF]/40 bg-[#A97BFF]/10 text-[#A97BFF]") {
                +"LVL $level"
            }
        }
        td(classes = "px-8 py-5 text-right text-[10px] text-slate-600 font-mono") {
            +id
        }
    }
}

// The rest of your UI components (framerCard, glowLayer, cardHeader, statusBadge) remain exactly as you have them.
fun FlowContent.framerCard(customClasses: String, block: DIV.() -> Unit) {
    div(classes = "relative bg-white/[0.03] border border-white/10 backdrop-blur-2xl rounded-[2rem] transition-all duration-500 hover:bg-white/[0.06] hover:border-white/20 $customClasses") {
        block()
    }
}

fun DIV.glowLayer() {
    div(classes = "absolute inset-0 opacity-0 hover:opacity-100 transition-opacity duration-700 pointer-events-none") {
        div(classes = "absolute -top-24 -left-24 w-48 h-48 bg-purple-500/20 blur-[60px]") {}
    }
}

fun DIV.cardHeader(title: String) {
    span(classes = "text-[11px] font-black tracking-[0.2em] text-slate-500 uppercase") {
        +title
    }
}

fun FlowContent.statusBadge(label: String, status: String, hexColor: String) {
    div(classes = "flex items-center gap-3 px-4 py-2 bg-white/5 border border-white/10 rounded-xl backdrop-blur-md shadow-sm") {
        div(classes = "w-2 h-2 rounded-full animate-pulse") {
            style = "background-color: $hexColor; box-shadow: 0 0 10px $hexColor;"
        }
        span(classes = "text-[10px] font-bold text-slate-400 tracking-wider") { +"$label" }
        span(classes = "text-[11px] font-black") {
            style = "color: $hexColor;"
            +status
        }
    }
}